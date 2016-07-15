/**
 * 
 */
package es.lcssl.sessions;

/**
 * @author lcu
 *
 */
public abstract class AbstractSession<
			F extends Factory<F, K, S>, 
			K extends Comparable<? super K>, 
			S extends AbstractSession<F, K, S>> 
implements  Session<F, K, S>
{
	K m_key;
	F m_factory;
	
	protected AbstractSession(F factory, K key) {
		m_key = key;
		m_factory = factory;
	}

	@Override
	public K getKey() {
		return m_key;
	}

	@Override
	public F getFactory() {
		return m_factory;
	}
}
