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
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Class to represent an origin of messages. It's managed as a Bean to be able
 * to process changes in nick, ident or hostname, so
 * {@link PropertyChangeListener}s can be notified when some of these change.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 */
public class Origin implements Serializable {

	private static final long serialVersionUID = 848206066966003608L;

	public static final String PROPERTY_NICK = "nick";
	public static final String PROPERTY_IDENT = "ident";
	public static final String PROPERTY_HOST = "host";

	String m_nick;
	String m_ident;
	String m_host;
	PropertyChangeSupport m_pcs;

	public Origin(String nick, String ident, String host) {
		m_nick = nick;
		m_ident = ident;
		m_host = host;
		m_pcs = new PropertyChangeSupport(this);
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

	/**
	 * Add a PropertyChangeListener to the listener list. The listener is
	 * registered for all properties. The same listener object may be added more
	 * than once, and will be called as many times as it is added. If listener
	 * is null, no exception is thrown and no action is taken.
	 * 
	 * @param listener
	 *            The {@link PropertyChangeListener} to be added
	 * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		m_pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Add a PropertyChangeListener for a specific property. The listener will
	 * be invoked only when a call on firePropertyChange names that specific
	 * property. The same listener object may be added more than once. For each
	 * property, the listener will be invoked the number of times it was added
	 * for that property. If propertyName or listener is null, no exception is
	 * thrown and no action is taken.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on.
	 * @param listener
	 *            The {@link PropertyChangeListener} to be added
	 * @see PropertyChangeSupport#addPropertyChangeListener(String,
	 *      PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		m_pcs.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * Remove a PropertyChangeListener from the listener list. This removes a
	 * PropertyChangeListener that was registered for all properties. If
	 * listener was added more than once to the same event source, it will be
	 * notified one less time after being removed. If listener is null, or was
	 * never added, no exception is thrown and no action is taken.
	 * 
	 * @param listener
	 *            The {@link PropertyChangeListener} to be removed.
	 * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		m_pcs.removePropertyChangeListener(listener);
	}

	/**
	 * Remove a PropertyChangeListener for a specific property. If listener was
	 * added more than once to the same event source for the specified property,
	 * it will be notified one less time after being removed. If propertyName is
	 * null, no exception is thrown and no action is taken. If listener is null,
	 * or was never added for the specified property, no exception is thrown and
	 * no action is taken.
	 * 
	 * @param propertyName
	 *            The name of the property that was listened on.
	 * @param listener
	 *            The PropertyChangeListener to be removed.
	 * @see PropertyChangeSupport#removePropertyChangeListener(String,
	 *      PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		m_pcs.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Reports a bound property update to listeners that have been registered to
	 * track updates of all properties or a property with the specified name.
	 * 
	 * <p>
	 * No event is fired if old and new values are equal and non-null.
	 * 
	 * @param propertyName
	 *            the programmatic name of the property that was changed
	 * @param oldValue
	 *            the old value of the property.
	 * @param newValue
	 *            the new value of the property.
	 * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
	 */
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public String toString() {
		return isServer() ? getHost()
				: isService() ? getNick() + "@" + getHost()
						: isUser() ? getNick() + "!" + getIdent() + "@" + getHost()
								: "<<UNKNOWN-" + getNick() + "!" + getIdent() + "@" + getHost() + ">>";
	}
}
