/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 27 de jun. de 2016 22:20:00.
 * Project: LadyAugustaAda
 * Package: es.lcssl.sessions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.sessions;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public abstract class AbstractSessionFactory<
		SF extends SessionFactory<SF,K,S>, 
		K extends Comparable<? super K>, 
		S extends AbstractSession<SF, K, S>>
implements SessionFactory<SF, K, S>
{
	Map<K, S> m_sessions;
	
	public AbstractSessionFactory() {
		m_sessions = new TreeMap<K, S>();
	}
	
	/**
	 * @param sessionKey
	 * @return
	 */
	protected abstract S newSession(K sessionKey);

	/**
	 * @param sessionKey
	 * @return
	 * @see es.lcssl.sessions.SessionFactory#lookup(java.lang.Comparable)
	 */
	@Override
	public S lookup(K sessionKey) {
		S result = m_sessions.get(sessionKey);
		if (result == null) {
			result = newSession(sessionKey);
		}
		return result;
	}

	/**
	 * @param sessionKey
	 * @see es.lcssl.sessions.SessionFactory#cancel(java.lang.Comparable)
	 */
	@Override
	public void cancel(K sessionKey) {
		S target = m_sessions.get(sessionKey);
		if (target == null) return;
		target.cancel();
		m_sessions.remove(sessionKey);
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.SessionFactory#getSessions()
	 */
	@Override
	public Collection<S> getSessions() {
		return m_sessions.values();
	}

}
