package es.lcssl.irc.monitors;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

public class PRIVMSGMonitor<S extends Session<S>> implements EventListener<Monitor,IRCCode,IRCMessage> {

	private String 						m_target;
	private Set<String> 				m_admins;
	private SessionFactory<Origin, S> 	m_sessionFactory;
	private Map<Origin, PRIVMSGSession> m_sessions;

	public PRIVMSGMonitor(String target, Set<String> admins, SessionFactory<Origin, S> factory) {
		m_target = target;
		m_admins = admins;
		m_sessionFactory = factory;
		m_sessions = new TreeMap<Origin, PRIVMSGSession>();
	}
	
	private class PRIVMSGSession extends Thread implements SessionManager<S> {

		private BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_queue;
		S m_session;
		Origin m_origin;
		Monitor m_monitor;
		
		public PRIVMSGSession(Origin key, Monitor monitor) {
			m_queue = new LinkedBlockingQueue<Event<Monitor,IRCCode,IRCMessage>>();
			m_session = m_sessionFactory.newSession(this, key);
			m_origin = key;
			m_monitor = monitor;
			setName(key.toString() + " >>> " + m_target); // the thread name is adjusted as the session id.
			m_sessions.put(key, this);
		}
		
		public void addEvent(Event<Monitor,IRCCode,IRCMessage> e) {
			m_queue.add(e);
		}

		// methods to be used by the receiving process.
		@Override
		public Event<Monitor, IRCCode, IRCMessage> getEvent() throws InterruptedException {
			return m_queue.poll();
		}
		@Override
		public Event<Monitor, IRCCode, IRCMessage> getEvent(long timespec, TimeUnit unit) 
				throws InterruptedException {
			return m_queue.poll(timespec, unit);
		}

		@Override
		public Monitor getMonitor() {
			return m_monitor;
		}

		// Override of the run method to pass the SessionManager to the thread implementing
		// the Session<S> interface.
		@Override
		public void run() {
			m_monitor.getIrcSAP().addMessage(new IRCMessage(
					IRCCode.NOTICE, 
					m_origin.getNick(), 
					"Creating session " + m_origin));
			int code = m_session.run(this);
			m_monitor.getIrcSAP().addMessage(new IRCMessage(
					IRCCode.NOTICE, 
					m_origin.getNick(), 
					"Terminating session " + m_origin + " with code " + code));
			synchronized(m_sessions) {
				m_sessions.remove(m_origin);
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
					res = new PRIVMSGSession(key, source);
					res.setDaemon(true);
					res.start();
				}
			}
		}
		return res;
	}
	
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		
		Monitor source 			  = event.getSource();
		IRCMessage message 		  = event.getMessage();
		Origin origin 			  = message.getOrigin();
		Collection<String> params = message.getParams();

		if (	   params.size() < 2 // invalid format 
				|| message.getCode() != IRCCode.PRIVMSG  // invalid message type 
				|| !m_target.equals(message.getParams().get(0))) // target does not match.
			return; // not for us.
		
		if (m_admins != null && !m_admins.contains(origin.getNick().toLowerCase())) {
			source.getIrcSAP().addMessage(new IRCMessage(
					IRCCode.NOTICE, 
					origin.getNick(), 
					"You are not in the list of admins"));
			return; // origin is not in admins for this application.
		}

		PRIVMSGSession session = lookup(origin, source); // get the session.
		session.addEvent(event); // and send the message to it.
	}
}
