package el.lcssl.irc.bot;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

public class PRIVMSGMonitor<S extends Session<S>> implements EventListener<Monitor,IRCCode,IRCMessage> {

	private String m_nick;
	
	private Set<String> m_admins;
	
	private SessionFactory<S> m_sessionFactory;
	
	private Map<String, PRIVMSGSession> m_sessions;
	
	private class PRIVMSGSession extends Thread implements SessionManager<S> {

		private BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_queue;
		S m_session;
		
		PRIVMSGSession(String id) {
			m_queue = new LinkedBlockingQueue<Event<Monitor,IRCCode,IRCMessage>>();
			setName(id); // the thread name is adjusted as the session id.
		}
				
		void addEvent(Event<Monitor,IRCCode,IRCMessage> e) {
			m_queue.add(e);
		}
		
		void setSession(S session) {
			m_session = session;
		}
		
		// methods to be used by the receiving process.
		@Override
		public Event<Monitor, IRCCode, IRCMessage> getEvent() throws InterruptedException {
			return m_queue.take();
		}
		
		@Override
		public String getNick() {
			return m_nick;
		}

		// Override of the run method to pass the SessionManager to the thread implementing
		// the Session<S> interface.
		@Override
		public void run() {
			m_session.run(this);
			synchronized(m_sessions) {
				m_sessions.remove(getName());
			}
		}
	} // PRIVMSGSession

	public PRIVMSGMonitor(String nick, String[] admins, SessionFactory<S> factory) {
		m_nick = nick;
		m_admins = new HashSet<String>();
		for (int i = 0; i < admins.length; i++)
			m_admins.add(admins[i]);
		m_sessionFactory = factory;
		m_sessions = new HashMap<String, PRIVMSGSession>();
	}
	
	@Override
	public void process(Monitor source,
			Event<Monitor, IRCCode, IRCMessage> event) {
		IRCMessage m = event.getMessage();
		Origin o = m.getOrigin();
		Collection<String> params = m.getParams();
		if (params.size() < 2) return; // invalid format.
		if (m.getCode() != IRCCode.PRIVMSG || !m_nick.equals(m.getParams().get(0)))
			return; // not a control message.

		PRIVMSGSession s = m_sessions.get(o.toString());

		if (s == null || !s.isAlive()) { // we have to create a new session.
			synchronized (m_sessions) {
				s = m_sessions.get(o.toString());
				if (s == null || !s.isAlive()) { // ask again in a protected environment.
					source.getIrcSAP().addMessage(new IRCMessage(
							IRCCode.NOTICE, 
							o.getNick(), 
							"Session not found or finished for " + o + ", creating one"));
					s = new PRIVMSGSession(o.toString());
					s.setSession(m_sessionFactory.newSession(s, o.toString()));
					m_sessions.put(o.toString(), s);
					s.start();
				}
			}
		}

		s.addEvent(event);
		
		// repeat the message back to Origin.
		source.getIrcSAP().addMessage(new IRCMessage(IRCCode.PRIVMSG, 
				m.getOrigin().getNick(), // nick from Origin in recv message. 
				">>> " + m.getParams().get(1))); // original message.
	}

}
