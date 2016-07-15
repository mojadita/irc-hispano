package es.lcssl.irc.monitors;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;
import es.lcssl.sessions.AbstractFactory;
import es.lcssl.sessions.AbstractSessionManager;
import es.lcssl.sessions.Session;

public abstract class PRIVMSGMonitor<
		S extends Session<PRIVMSGMonitor<S>,PRIVMSGMonitor<S>.PRIVMSGSession,Origin,S>> 
extends AbstractFactory<PRIVMSGMonitor<S>, PRIVMSGMonitor<S>.PRIVMSGSession, Origin, S>
implements EventListener<Monitor,IRCCode,IRCMessage> {

	private String 						m_target;
	private Set<String> 				m_admins;

	public PRIVMSGMonitor(String target, Set<String> admins, Properties config) {
		m_target = target;
		m_admins = admins;
	}
	
	public class PRIVMSGSession 
	extends AbstractSessionManager<PRIVMSGMonitor<S>, PRIVMSGSession, Origin, S> {

		S m_session;
		
		public PRIVMSGSession(S session) {
			super(PRIVMSGMonitor.this, session);
			m_session = session;
		}
	} // PRIVMSGSession
	
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		
//		long timestamp			  = event.getTimestamp();
		Monitor source 			  = event.getSource();
		IRCMessage message 		  = event.getMessage();
		Origin origin 			  = message.getOrigin();
		Collection<String> params = message.getParams();

		if (	   params.size() < 2 // invalid format 
				|| message.getCode() != IRCCode.PRIVMSG  // invalid message type 
				|| !m_target.equalsIgnoreCase(message.getParams().get(0))) // target does not match.
			return; // not for us.
		
		if (m_admins != null && !m_admins.contains(origin.getNick().toLowerCase())) {
			source.getIrcSAP().addMessage(new IRCMessage(
					IRCCode.NOTICE, 
					origin.getNick(), 
					"You are not in the list of admins"));
			return; // origin is not in admins for this application.
		}

		S session = lookupSession(origin); // get the session.
		try {
			session.getSessionManager().putEvent(event);
		} catch (InterruptedException e) {
			// this should not happen with the selected implementation.
			e.printStackTrace();
		} // and send the message to it.
	}

	@Override
	protected PRIVMSGMonitor<S>.PRIVMSGSession newSessionManager(S session) {
		return new PRIVMSGSession(session);
	}
}
