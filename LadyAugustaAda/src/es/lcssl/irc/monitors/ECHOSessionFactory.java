/**
 * 
 */
package es.lcssl.irc.monitors;

import java.util.Properties;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;
import es.lcssl.sessions.AbstractSessionFactory;
import es.lcssl.sessions.AbstractSessionManager;

/**
 * @author lcu
 *
 */
public class ECHOSessionFactory 
extends AbstractSessionFactory<
			ECHOSessionFactory, 
			ECHOSessionFactory.ECHOSessionManager, 
			Origin, 
			ECHOSession> 
implements EventListener<Monitor,IRCCode,IRCMessage>
{

	IrcSAP m_sap;
	
	public ECHOSessionFactory(IrcSAP sap, Properties props) {
		m_sap = sap;
	}
	
	public class ECHOSessionManager
	extends AbstractSessionManager<
			ECHOSessionFactory, 
			ECHOSessionManager, 
			Origin, 
			ECHOSession> 
	{
		public ECHOSessionManager() {
			super(ECHOSessionFactory.this);
		}
	}

	public void setup() {
		m_sap.getInputMonitor().register(IRCCode.PRIVMSG, this);
	}
	
	public void clean() {
		m_sap.getInputMonitor().unregister(IRCCode.PRIVMSG, this);
	}
	
	/**
	 * @param key
	 * @return
	 * @see es.lcssl.sessions.AbstractSessionFactory#newSession(java.lang.Comparable)
	 */
	@Override
	protected ECHOSession newSession(Origin key) {
		return new ECHOSession(key);
	}

	@Override
	protected ECHOSessionManager newSessionManager() {
		return new ECHOSessionManager();
	}

	/**
	 * @param event
	 * @see es.lcssl.irc.protocol.EventListener#process(es.lcssl.irc.protocol.Event)
	 */
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		System.out.println("process");
		IRCMessage message = event.getMessage();
		ECHOSession session = lookupSession(message.getOrigin());
		ECHOSessionManager sessionManager = session.getSessionManager();
		try {
			sessionManager.putEvent(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}