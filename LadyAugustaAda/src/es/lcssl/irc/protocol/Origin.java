/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 22 de abr. de 2016, 20:43:13
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.beans.PropertyChangeListener;

/**
 * Class to represent an origin of messages. It's managed as a Bean to be able
 * to process changes in nick, ident or hostname, so
 * {@link PropertyChangeListener}s can be notified when some of these change.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 */
public class Origin extends Common {

	private static final long serialVersionUID = -4815682808442452140L;

	public static final String PROPERTY_NICK = "nick";
	public static final String PROPERTY_IDENT = "ident";
	public static final String PROPERTY_HOST = "host";

	private String m_nick;
	private String m_ident;
	private String m_host;

	public Origin(String nick, String ident, String host) {
		m_nick = nick;
		m_ident = ident;
		m_host = host;
	}

	public Origin(String item) {
		int atPos = item.indexOf('@');
		if (atPos >= 0) {
			m_host = item.substring(atPos + 1);
			item = item.substring(0, atPos);
			int exclPos = item.indexOf('!');
			if (exclPos >= 0) {
				m_nick = item.substring(0, exclPos);
				m_ident = item.substring(exclPos + 1);
			} else {
				m_nick = item;
			}
		} else {
			m_host = item;
		}
	}
	
	public String protocolValue() {
		return m_nick;
	}

	/**
	 * Getter for the {@code String nick} property.
	 * 
	 * @return the {@code String} value of the property {@code nick}
	 */
	public String getNick() {
		return m_nick;
	}

	/**
	 * Setter for {@code String} property {@code nick}.
	 * 
	 * @param nick
	 *            the {@code String} value to set nick property to.
	 */
	public void setNick(String nick) {
		String oldNick = getNick();
		if (oldNick != null && oldNick.equals(nick))
			return;
		if (nick != null && nick.equals(oldNick))
			return;
		if (oldNick == nick)
			return;
		m_nick = nick;
		firePropertyChange(PROPERTY_NICK, oldNick, nick);
	}

	/**
	 * Getter for the {@code String ident} property.
	 * 
	 * @return the {@code String} value of the property {@code ident}
	 */
	public String getIdent() {
		return m_ident;
	}

	/**
	 * Setter for {@code String} property {@code ident}.
	 * 
	 * @param ident
	 *            the {@code String} value to set ident property to.
	 */
	public void setIdent(String ident) {
		String oldIdent = getIdent();
		if (oldIdent != null && oldIdent.equals(ident))
			return;
		if (ident != null && ident.equals(oldIdent))
			return;
		if (oldIdent == ident)
			return;
		m_ident = ident;
		firePropertyChange(PROPERTY_IDENT, oldIdent, ident);
	}

	/**
	 * Getter for the {@code String host} property.
	 * 
	 * @return the {@code String} value of the property {@code host}
	 */
	public String getHost() {
		return m_host;
	}

	/**
	 * Setter for {@code String} property {@code host}.
	 * 
	 * @param host
	 *            the {@code String} value to set host property to.
	 */
	public void setHost(String host) {
		String oldHost = getHost();
		if (oldHost != null && oldHost.equals(host))
			return;
		if (host != null && host.equals(oldHost))
			return;
		if (oldHost == host)
			return;
		m_host = host;
		firePropertyChange(PROPERTY_HOST, oldHost, host);
	}

	/**
	 * Getter for the {@code boolean} {@code server} property.
	 * <p>
	 * This property tells from the existent components of the Origin mask if it
	 * matches the mask of a server (only host part included)
	 * 
	 * @return {@code true} if only the host component of the mask is present.
	 */
	public boolean isServer() {
		return getNick() == null && getIdent() == null && getHost() != null;
	}

	/**
	 * Getter for the {@code boolean} {@code service} property.
	 * <p>
	 * This property tells from the existent components of the Origin mask if it
	 * matches the mask of a services bot (only nick and hostname parts
	 * included, no ident).
	 * 
	 * @return {@code true} if the Origin represents an IRC services bot.
	 */
	public boolean isService() {
		return getNick() != null && getIdent() == null && getHost() != null;
	}

	/**
	 * Getter for the {@code boolean} {@code user} property.
	 * <p>
	 * This property tells from the existent components of the Origin mask if it
	 * matches tha mask of a normal user (or ircop, the three parts must be
	 * included for a normal user)
	 * 
	 * @return {@code true} if the {@link Origin} represents a normal IRC user.
	 */
	public boolean isUser() {
		return getNick() != null && getIdent() != null && getHost() != null;
	}

	/**
	 * Getter for the {@code boolean} {@code other} property.
	 * <p>
	 * This property tells from the existent mask components if it represents
	 * other kinds of mask.
	 * 
	 * @return {@code true} if the Origin represents neither a server, nor a
	 *         service bot, nor a normal user.
	 */
	public boolean isOther() {
		return !isServer() && !isService() && !isUser();
	}

	@Override
	public String toString() {
		return isServer() ? getHost()
				: isService() ? getNick() + "@" + getHost()
						: isUser() ? getNick() + "!" + getIdent() + "@" + getHost()
								: "<<UNKNOWN-" + getNick() + "!" + getIdent() + "@" + getHost() + ">>";
	}

}
