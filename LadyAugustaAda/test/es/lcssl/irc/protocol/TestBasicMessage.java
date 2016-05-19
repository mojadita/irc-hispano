/**
 * 
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the abstract {@link BasicMessage} class.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 *
 */
public class TestBasicMessage {
	
	BasicMessage m_iut;
	PropertyChangeListener m_pcl;
	PropertyChangeEvent m_pce;
	
	@Before
	public void init() {
		m_iut = new BasicMessage() {
			private static final long serialVersionUID = -5944371962799994512L;
			@Override
			public String protocolValue() {
				return null;
			}
		};
		m_pcl = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				m_pce = evt;
			}
		};
		m_iut.addPropertyChangeListener(m_pcl);
	}

	@Test
	public void testOrigin() {
		assertNull(m_pce);
		Origin o = new Origin("a", "b", "c.d.com");
		m_iut.setOrigin(o);
		assertNotNull(m_pce);
		assertSame(m_iut, m_pce.getSource());
		assertEquals(BasicMessage.PROPERTY_ORIGIN, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertSame(o, m_pce.getNewValue());
		assertSame(o, m_iut.getOrigin());
		
	}

	@Test
	public void testParameters() {
		assertNull(m_pce);
		List<String> params = new ArrayList<String>();
		params.add("a");
		params.add("b");
		params.add("c");
		m_iut.setParams(params);
		assertNotNull(m_pce);
		assertSame(m_iut, m_pce.getSource());
		assertEquals(BasicMessage.PROPERTY_PARAMS, m_pce.getPropertyName());
		assertNotNull(m_pce.getOldValue());
		assertSame(params, m_pce.getNewValue());
		// not same, but equals.
		assertEquals(params, m_iut.getParams());
	}
	
	@Test
	public void testParameters2() {
		assertNull(m_pce);
		m_iut.setParams("a", "b", "c");
		assertNotNull(m_pce);
		assertSame(m_iut, m_pce.getSource());
		assertEquals(BasicMessage.PROPERTY_PARAMS, m_pce.getPropertyName());
		Iterator<String> i = m_iut.getParams().iterator();
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}
	
}
