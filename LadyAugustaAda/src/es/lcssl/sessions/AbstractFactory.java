/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 2 de jul. de 2016 21:17:03.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.monitors
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 * @param <F>
 * @param <K>
 * @param <S>
 */
public abstract class AbstractFactory<
		F extends AbstractFactory<F, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<F, K, S>> 
implements Factory<F, K, S>,
		   EventListener<Monitor, IRCCode, IRCMessage>
{
	private Map<K, S> m_sessionMap;
	
	public AbstractFactory() {
		m_sessionMap = new TreeMap<K, S>();
	}
	
	/**
	 * delegated method to call the proper constructor to generate a
	 * new {@code S} session from the passed {@code key}.  It has already
	 * check that the key is not present in the {@link Factory} so it is valid
	 * to directly call the proper constructor.
	 * @param key is the session key to construct the session.
	 * @return a valid session with key {@code key}.
	 */
	protected abstract S newSession(K key);
	
	/**
	 * Delegated method to generate the session key from the received {@code event}.
	 * @param event is the event received from the {@link Monitor} instance.
	 * @return the {@code K key} corresponding to the session this event relates to.
	 */
	protected abstract K getSessionKey(Event<Monitor, IRCCode, IRCMessage> event);
	
	/**
	 * Delegate method to construct a new event from the one we received from the {@link Monitor}.
	 * @param oldEvent is the event we received.
	 * @return is a properly constructed event with data translated from the {@code oldEvent}
	 */
	protected abstract Event<Monitor, IRCCode, IRCMessage> newEvent(Event<Monitor, IRCCode, IRCMessage> oldEvent);

	/**
	 * @see es.lcssl.sessions.Factory#lookupSession(java.lang.Comparable)
	 */
	@Override
	public S lookupSession(K key) {
		S session = m_sessionMap.get(key);
		if (session == null) { // no session at all.
			synchronized(m_sessionMap) {
				session = m_sessionMap.get(key);
				if (session == null) {
					System.out.println("CREATING session [" + key + "]");
					m_sessionMap.put(key, session = newSession(key));
				}
			}
		}
		return session;
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.Factory#getSessions()
	 */
	@Override
	public List<S> getSessions() {
		synchronized(m_sessionMap) {
			ArrayList<S> values = new ArrayList<S>();
			values.addAll(m_sessionMap.values());
			return values;
		}
	}

	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		lookupSession(getSessionKey(event)).process(event);
	}
}
