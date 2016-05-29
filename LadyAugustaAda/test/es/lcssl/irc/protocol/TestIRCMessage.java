/**
 * 
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the abstract {@link IRCMessage} class.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 *
 */
public class TestIRCMessage {
	
	IRCMessage m_iut;
	PropertyChangeListener m_pcl;
	PropertyChangeEvent m_pce;
	
	@Before
	public void init() {
		m_iut = new IRCMessage(IRCCode.ERR_NORECIPIENT);
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
		assertNull(m_iut.getOrigin());
		assertEquals(IRCCode.ERR_NORECIPIENT, m_iut.getCode());
		assertArrayEquals(new String[]{}, m_iut.getParams().toArray());
		assertNull(m_pce);
		Origin o = new Origin("a", "b", "c.d.com");
		m_iut.setOrigin(o);
		assertNotNull(m_pce);
		assertSame(m_iut, m_pce.getSource());
		assertEquals(IRCMessage.PROPERTY_ORIGIN, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertSame(o, m_pce.getNewValue());
		assertSame(o, m_iut.getOrigin());
		
	}
	
	@Test
	public void testCode() {
		assertEquals(IRCCode.ERR_NORECIPIENT, m_iut.getCode());
		assertNull(m_pce);
		m_iut.setCode(IRCCode.AWAY);
		assertNotNull(m_pce);
		assertSame(m_iut, m_pce.getSource());
		assertEquals(IRCMessage.PROPERTY_CODE, m_pce.getPropertyName());
		assertEquals(IRCCode.ERR_NORECIPIENT, m_pce.getOldValue());
		assertSame(IRCCode.AWAY, m_pce.getNewValue());
		assertSame(IRCCode.AWAY, m_iut.getCode());
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
		assertEquals(IRCMessage.PROPERTY_PARAMS, m_pce.getPropertyName());
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
		assertEquals(IRCMessage.PROPERTY_PARAMS, m_pce.getPropertyName());
		assertArrayEquals(new String[]{"a", "b", "c"}, m_iut.getParams().toArray());
	}
	
	@Test
	public void testToString() {
		m_iut.setParams("a", "b", "c");
		m_iut.setOrigin(new Origin("nick", "ident", "host.com"));
		assertEquals(":nick!ident@host.com 411 a b c", m_iut.toString());
	}
	
	@Test
	public void testGetBytes() {
		m_iut.setParams("a", "b", "c");
		m_iut.setOrigin(new Origin("nick", "ident", "host.com"));
		assertArrayEquals(
				new byte[] {
						':','n','i','c','k','!','i','d','e','n','t','@','h','o','s','t','.',
						'c','o','m',' ','4','1','1',' ','a',' ','b',' ','c','\r','\n',
				}, m_iut.getBytes());
	}
	
}
