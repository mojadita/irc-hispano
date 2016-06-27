/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 27 de jun. de 2016 22:33:27.
 * Project: LadyAugustaAda
 * Package: es.lcssl.sessions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.sessions;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public abstract class AbstractSession<
		SF extends SessionFactory<SF, K, S>, 
		K extends Comparable<? super K>, 
		S extends AbstractSession<SF, K, S>>
implements Session<SF, K, S> {
	
	SF m_sessionFactory;
	K m_key;
	
	/**
	 * @param sessionFactory
	 * @param key
	 */
	AbstractSession(SF sessionFactory, K key) {
		m_sessionFactory = sessionFactory;
		m_key = key;
	}
	
	protected abstract void cancel();

	/**
	 * Getter for the {@code SF} {@code sessionFactory} attribute.
	 * @return the value of the {@code sessionFactory}.
	 */
	public SF getSessionFactory() {
		return m_sessionFactory;
	}

	/**
	 * Getter for the {@code K} {@code key} attribute.
	 * @return the value of the {@code key}.
	 */
	public K getKey() {
		return m_key;
	}
}
