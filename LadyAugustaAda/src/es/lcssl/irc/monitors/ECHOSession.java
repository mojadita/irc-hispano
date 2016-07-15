/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 3 de jul. de 2016 21:34:06.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.monitors
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.monitors;

import es.lcssl.irc.monitors.ECHOSessionFactory.ECHOSessionManager;
import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;
import es.lcssl.sessions.Session;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class ECHOSession 
implements Session<ECHOSessionFactory, ECHOSessionManager, Origin, ECHOSession> {
	
	private Origin m_origin;
	private ECHOSessionManager m_sessionManager;
	
	public ECHOSession(Origin key) {
		m_origin = key; 
	}

	/**
	 * @param sessionManager
	 * @return
	 * @see es.lcssl.sessions.Session#run(es.lcssl.sessions.SessionManager)
	 */
	@Override
	public int run() {
		try {
			long oldTimestamp = 0;
			Event<Monitor,IRCCode,IRCMessage> event = null; 
			for (int i = 0; i < 10; i++) {
				event = getSessionManager().getEvent();
				Monitor monitor = event.getSource();
				long timestamp = event.getTimestamp();
				if (oldTimestamp == 0) oldTimestamp = timestamp;
				long lap = timestamp - oldTimestamp;
				IRCMessage message = event.getMessage();
				String to = message.getOrigin().getNick();
				monitor.getIrcSAP().addMessage(new IRCMessage(
						IRCCode.PRIVMSG, 
						to,
						">>>(" + i + ", " + String.format("%d.%03d", lap/1000, lap%1000) + "): " + message.getParams().get(1)));
				oldTimestamp = timestamp;
			}
			if (event != null) {
				event.getSource().getIrcSAP().addMessage(new IRCMessage(
						IRCCode.NOTICE, 
						event.getMessage().getOrigin().getNick(),
						">>> SESSION FINISHED " + event.getMessage().getOrigin()));

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.Session#getKey()
	 */
	@Override
	public Origin getKey() {
		return m_origin;
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.Session#getSessionManager()
	 */
	@Override
	public ECHOSessionManager getSessionManager() {
		return m_sessionManager;
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.Session#getFactory()
	 */
	@Override
	public ECHOSessionFactory getFactory() {
		return m_sessionManager.getSessionFactory();
	}

	/**
	 * @param sessionManager
	 * @see es.lcssl.sessions.Session#setSessionManager(es.lcssl.sessions.SessionManager)
	 */
	@Override
	public void setSessionManager(ECHOSessionManager sessionManager) {
		m_sessionManager = sessionManager;
	}

}
