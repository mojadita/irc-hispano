/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 2 de jul. de 2016 21:17:03.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.monitors
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.sessions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public abstract class AbstractSessionFactory<
		SF extends AbstractSessionFactory<SF, SM, K, S>, 
		SM extends SessionManager<SF, SM, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<SF, SM, K, S>> 
extends Thread
implements SessionFactory<SF, SM, K, S> {

	private Map<K, SM> m_sessionManagers;
	
	public AbstractSessionFactory() {
		m_sessionManagers = new TreeMap<K, SM>();
	}
	
	protected abstract SM newSessionManager(S session);
	protected abstract S newSession(K key);

	/**
	 * @see es.lcssl.sessions.SessionFactory#lookupSession(java.lang.Comparable)
	 */
	@Override
	public S lookupSession(K key) {
		SM sessionManager = m_sessionManagers.get(key);
		if (sessionManager == null) { // no sessionManager ==> no session at all.
			sessionManager = newSessionManager(newSession(key));
			Thread thread = new Thread(sessionManager);
			thread.setName(thread + "[" + key + "]");
			thread.start();
			// now, let's allow the world to see it.
			m_sessionManagers.put(key, sessionManager);
		}
		return sessionManager.getSession();
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.SessionFactory#getSessionManagers()
	 */
	@Override
	public Collection<SM> getSessionManagers() {
		return m_sessionManagers.values();
	}

	/**
	 * @return
	 * @see es.lcssl.sessions.SessionFactory#getSessions()
	 */
	@Override
	public synchronized Collection<S> getSessions() {
		ArrayList<S> values = new ArrayList<S>();
		for (SM sessionManager: m_sessionManagers.values())
			values.add(sessionManager.getSession());
		return values;
	}
}
