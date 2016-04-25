/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 23 de abr. de 2016, 11:39:34
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import es.lcssl.irc.protocol.Channel.TargetType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link Origin} class.
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class TestChannel {

	public static final String CHANNEL = "channel";
	public static final String CHANNEL_GLOBAL = "#channel";
	public static final String CHANNEL_MODELESS = "+channel";
	public static final String CHANNEL_LOCAL = "&channel";

	Channel m_instance;
	PropertyChangeEvent m_pce;

	PropertyChangeListener m_mockListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			m_pce = evt;
		}
	};

	@Before
	public void init() {
		m_instance = new Channel(null, null);
		m_instance.addPropertyChangeListener(m_mockListener);
	}

	@Test
	public void testCreation() {
		assertNotNull(m_instance);
	}

	@Test
	public void testIdProperty() {
		// must be null, as we have instantiated it with a null nick.
		assertNull(m_instance.getType());
		assertNull(m_instance.getId());
		// must be null, as we have not already made any set operation.
		assertNull(m_pce);
		m_instance.setId("ABCDEF");
		assertNotNull(m_pce); // m_mockListener must have set the m_pce value to
								// a non null event.
		assertSame(m_instance, m_pce.getSource());
		assertEquals(Channel.PROPERTY_ID, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertEquals("ABCDEF", m_pce.getNewValue());
		assertEquals("ABCDEF", m_instance.getId());
	}
	
	@Test
	public void testConstructorStringNormal() {
		Channel c = new Channel("#ABCDEF");
		assertSame(Channel.TargetType.NORMAL_CHANNEL, c.getType());
		assertEquals("ABCDEF", c.getId());
		assertEquals("#ABCDEF", c.protocolValue());
	}
	
	@Test
	public void testConstructorStringLocal() {
		Channel c = new Channel("&ABCDEF");
		assertSame(Channel.TargetType.LOCAL_CHANNEL, c.getType());
		assertEquals("ABCDEF", c.getId());
		assertEquals("&ABCDEF", c.protocolValue());
	}
	
	@Test
	public void testConstructorStringModeless() {
		Channel c = new Channel("+ABCDEF");
		assertSame(Channel.TargetType.MODELESS_CHANNEL, c.getType());
		assertEquals("ABCDEF", c.getId());
		assertEquals("+ABCDEF", c.protocolValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorStringIllegal() {
		Channel c = new Channel("ABCDEF");
	}

	@Test
	public void testIdTypeAndProtocolValue() {
		// must be null, as we have instantiated it with a null nick.
		assertNull(m_instance.getType());
		assertNull(m_instance.getId());
		
		// must be null, as we have not already made any set operation.
		assertNull(m_pce);
		m_instance.setType(TargetType.LOCAL_CHANNEL);
		assertNotNull(m_pce); // m_mockListener must have set the m_pce value to
								// a non null event.
		assertSame(m_instance, m_pce.getSource());
		assertEquals(Channel.PROPERTY_TYPE, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertSame(TargetType.LOCAL_CHANNEL, m_pce.getNewValue());
		assertSame(TargetType.LOCAL_CHANNEL, m_instance.getType());
		m_instance.setId("ABCDEF");
		assertNotNull(m_pce); // m_mockListener must have set the m_pce value to
								// a non null event.
		assertSame(m_instance, m_pce.getSource());
		assertEquals(Channel.PROPERTY_ID, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertEquals("ABCDEF", m_pce.getNewValue());
		assertEquals(TargetType.LOCAL_CHANNEL, m_instance.getType());
		assertEquals("&ABCDEF", m_instance.protocolValue());
	}

}
