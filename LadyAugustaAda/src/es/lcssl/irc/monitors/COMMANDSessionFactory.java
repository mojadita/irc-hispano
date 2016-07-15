package es.lcssl.irc.monitors;

import java.util.Properties;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;
import es.lcssl.sessions.AbstractFactory;
import es.lcssl.sessions.Session;

/**
 * This class implements a listener to conduct shell sessions to this bot.
 * For each {@link Origin} a new {@link COMMANDSession} is created and
 * a thread is associated to receive messages directed to us.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}.
 */
public class COMMANDSessionFactory 
extends AbstractFactory<COMMANDSessionFactory, Origin, COMMANDSession> 
{
	
	private IrcSAP m_sap;
	public Properties m_properties;
	
	public COMMANDSessionFactory(IrcSAP sap, Properties properties) {
		m_sap = sap;
		m_properties = properties;
		register();
	}
	
	public void register() {
		m_sap.getInputMonitor().register(IRCCode.PRIVMSG, this); // commands.
		m_sap.getInputMonitor().register(IRCCode.QUIT, this); // for end of session.
		m_sap.getInputMonitor().register(IRCCode.NICK, this); // for change of nick.
	}

	public void unregister() {
		m_sap.getInputMonitor().unregister(IRCCode.PRIVMSG, this); // commands.
		m_sap.getInputMonitor().unregister(IRCCode.QUIT, this); // for end of session.
		m_sap.getInputMonitor().unregister(IRCCode.NICK, this); // for change of nick.
	}

	public IrcSAP getSap() {
		return m_sap;
	}

	public Properties getProperties() {
		return m_properties;
	}

	/**
	 * @see es.lcssl.sessions.AbstractFactory#newSession(java.lang.Comparable)
	 */
	@Override
	protected COMMANDSession newSession(Origin key) {
		return new COMMANDSession(this, key, m_properties);
	}

	/**
	 * This delegate method obtains the {@link Session} key from the received event. It
	 * is used to select which {@link Session} corresponds to the recently received message.
	 * @see es.lcssl.sessions.AbstractFactory#getSessionKey(es.lcssl.irc.protocol.Event)
	 */
	@Override
	protected Origin getSessionKey(Event<Monitor, IRCCode, IRCMessage> event) {
		return event.getMessage().getOrigin();
	}

}
