/**
 * 
 */
package es.lcssl.sessions;

import java.util.Properties;

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
	Properties m_properties;
	
	protected AbstractSession(F factory, K key, Properties properties) {
		m_key = key;
		m_factory = factory;
		m_properties = properties;
	}

	@Override
	public K getKey() {
		return m_key;
	}

	@Override
	public F getFactory() {
		return m_factory;
	}
	
	public Properties getProperties() {
		return m_properties;
	}
	
}
