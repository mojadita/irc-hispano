package el.lcssl.irc.bot;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

public class SessionPRIVMSGMonitor<S extends Session<S>> implements EventListener<Monitor,IRCCode,IRCMessage> {

	private String m_target;
	private Set<String> m_admins;
	private SessionFactory<Origin, S> m_sessionFactory;
	private Map<Origin, PRIVMSGSession> m_sessions;

	public SessionPRIVMSGMonitor(String target, Set<String> admins, SessionFactory<Origin, S> factory) {
		m_target = target;
		m_admins = admins;
		m_sessionFactory = factory;
		m_sessions = new TreeMap<Origin, PRIVMSGSession>();
	}
	
	private class PRIVMSGSession extends Thread implements SessionManager<S> {

		private BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_queue;
		S m_session;
		
		public PRIVMSGSession(Origin key) {
			m_queue = new LinkedBlockingQueue<Event<Monitor,IRCCode,IRCMessage>>();
			m_session = m_sessionFactory.newSession(this, key);
			setName(key.toString() + " >>> " + m_target); // the thread name is adjusted as the session id.
		}
		
		public void addEvent(Event<Monitor,IRCCode,IRCMessage> e) {
			m_queue.add(e);
		}
		
		// methods to be used by the receiving process.
		@Override
		public Event<Monitor, IRCCode, IRCMessage> getEvent() throws InterruptedException {
			return m_queue.take();
		}
		
		@Override
		public String getTarget() {
			return m_target;
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
	
	private PRIVMSGSession lookup(Origin key, Monitor source) {
		PRIVMSGSession res = m_sessions.get(key);
		if (res == null || !res.isAlive()) { // we have to create a new session.
			synchronized (m_sessions) {
				res = m_sessions.get(key); // repeat to ensure we have a dead session or inexistent.
				if (res == null || !res.isAlive()) { // ask again in a protected environment.
					// signal to origin we are creating a new session.
					source.getIrcSAP().addMessage(new IRCMessage(
							IRCCode.NOTICE, 
							key.getNick(), 
							"Session not found or finished for " + key + ", creating one"));
					res = new PRIVMSGSession(key);
					m_sessions.put(key, res);
					res.start();
				}
			}
		}
		return res;
	}
	
	@Override
	public void process(Monitor source,
			Event<Monitor, IRCCode, IRCMessage> event) {
		
		IRCMessage message = event.getMessage();
		Origin origin = message.getOrigin();

		if (m_admins != null && !m_admins.contains(origin.getNick())) 
			return; // origin is not in admins for this application.

		Collection<String> params = message.getParams();
		if (params.size() < 2) 
			return; // invalid format.
		if (	   params.size() < 2 // invalid format 
				|| message.getCode() != IRCCode.PRIVMSG  // invalid message type 
				|| !m_target.equals(message.getParams().get(0))) // target does not match.
			return; // not for us.
		
		PRIVMSGSession session = lookup(origin, source); // get the session.
		session.addEvent(event); // and send the message to it.
		
	}
}
