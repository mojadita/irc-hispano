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
public class TestOrigin {

	public static final String NICK = "nick";
	public static final String USER_NAME = "userName";
	public static final String DOMAIN = "host.domain.com";

	Origin m_instance;
	PropertyChangeEvent m_pce;
	static final String theNick = "mojadita";
	static final String theIdent = "kania";
	static final String theHost = "te.llevo.en.mi.corazon.virtual";

	PropertyChangeListener m_mockListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			m_pce = evt;
		}
	};

	@Before
	public void init() {
		m_instance = new Origin(null, null, null);
		m_instance.addPropertyChangeListener(m_mockListener);
	}

	@Test
	public void testCreation() {
		assertNotNull(m_instance);
	}

	@Test
	public void testNickProperty() {
		// must be null, as we have instantiated it with a null nick.
		assertNull(m_instance.getNick());
		// must be null, as we have not already made any set operation.
		assertNull(m_pce);
		m_instance.setNick(theNick);
		assertNotNull(m_pce); // m_mockListener must have set the m_pce value to
								// a non null event.
		assertSame(m_instance, m_pce.getSource());
		assertEquals(Origin.PROPERTY_NICK, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertSame(theNick, m_pce.getNewValue());
		assertSame(theNick, m_instance.getNick());
	}

	@Test
	public void testIdentProperty() {
		// must be null, as we have instantiated it with a null nick.
		assertNull(m_instance.getIdent());
		// must be null, as we have not already made any set operation.
		assertNull(m_pce);
		m_instance.setIdent(theIdent);
		assertNotNull(m_pce); // m_mockListener must have set the m_pce value to
								// a non null event.
		assertSame(m_instance, m_pce.getSource());
		assertEquals(Origin.PROPERTY_IDENT, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertSame(theIdent, m_pce.getNewValue());
		assertSame(theIdent, m_instance.getIdent());
	}

	@Test
	public void testHostProperty() {
		// must be null, as we have instantiated it with a null nick.
		assertNull(m_instance.getHost());
		// must be null, as we have not already made any set operation.
		assertNull(m_pce);
		m_instance.setHost(theHost);
		assertNotNull(m_pce); // m_mockListener must have set the m_pce value to
								// a non null event.
		assertSame(m_instance, m_pce.getSource());
		assertEquals(Origin.PROPERTY_HOST, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertSame(theHost, m_pce.getNewValue());
		assertSame(theHost, m_instance.getHost());
	}

	@Test
	public void testNickListenerAndSetNick() {
		m_instance = new Origin(null, null, null);
		assertNull(m_instance.getNick());
		assertNull(m_instance.getIdent());
		assertNull(m_instance.getHost());
		m_instance.addPropertyChangeListener(Origin.PROPERTY_NICK, m_mockListener);
		assertNull(m_pce);
		m_instance.setIdent(theIdent);
		assertNull(m_pce);
		assertEquals(theIdent, m_instance.getIdent());
		m_instance.setNick(theNick);
		assertNotNull(m_pce);
		assertEquals(m_instance, m_pce.getSource());
		assertEquals(Origin.PROPERTY_NICK, m_pce.getPropertyName());
		assertNull(m_pce.getOldValue());
		assertEquals(theNick, m_pce.getNewValue());
		assertEquals(theNick, m_instance.getNick());
		assertNull(m_instance.getHost());
	}

	@Test
	public void testNickListenerAndSetNonNick() {
		m_instance = new Origin(null, null, null);
		assertNull(m_instance.getNick());
		assertNull(m_instance.getIdent());
		assertNull(m_instance.getHost());
		m_instance.addPropertyChangeListener(Origin.PROPERTY_NICK, m_mockListener);
		assertNull(m_pce);
		m_instance.setIdent(theIdent);
		assertNull(m_pce); // the listener was not added for PROPERTY_IDENT
		assertNull(m_instance.getNick());
		assertEquals(theIdent, m_instance.getIdent());
		assertNull(m_instance.getHost());
	}

	@Test
	public void testIsServer() {
		assertFalse(m_instance.isServer()); // null, null, null
		m_instance.setHost(theHost);
		assertTrue(m_instance.isServer()); // null, null, theHost
		m_instance.setIdent(theIdent);
		assertFalse(m_instance.isServer()); // null, theIdent, theHost
		m_instance.setHost(null);
		assertFalse(m_instance.isServer()); // null, theIdent, null
		m_instance.setNick(theNick);
		assertFalse(m_instance.isServer()); // theNick, theIdent, null
		m_instance.setHost(theHost);
		assertFalse(m_instance.isServer()); // theNick, theIdent, theHost
		m_instance.setIdent(null);
		assertFalse(m_instance.isServer()); // theNick, null, theHost
		m_instance.setHost(null);
		assertFalse(m_instance.isServer()); // theNick, null, null
		m_instance.setNick(null);
		assertFalse(m_instance.isServer()); // null, null, null
	}

	@Test
	public void testIsUser() {
		assertFalse(m_instance.isUser()); // null, null, null
		m_instance.setHost(theHost);
		assertFalse(m_instance.isUser()); // null, null, theHost
		m_instance.setIdent(theIdent);
		assertFalse(m_instance.isUser()); // null, theIdent, theHost
		m_instance.setHost(null);
		assertFalse(m_instance.isUser()); // null, theIdent, null
		m_instance.setNick(theNick);
		assertFalse(m_instance.isUser()); // theNick, theIdent, null
		m_instance.setHost(theHost);
		assertTrue(m_instance.isUser()); // theNick, theIdent, theHost
		m_instance.setIdent(null);
		assertFalse(m_instance.isUser()); // theNick, null, theHost
		m_instance.setHost(null);
		assertFalse(m_instance.isUser()); // theNick, null, null
		m_instance.setNick(null);
		assertFalse(m_instance.isUser()); // null, null, null
	}

	@Test
	public void testIsService() {
		assertFalse(m_instance.isService()); // null, null, null
		m_instance.setHost(theHost);
		assertFalse(m_instance.isService()); // null, null, theHost
		m_instance.setIdent(theIdent);
		assertFalse(m_instance.isService()); // null, theIdent, theHost
		m_instance.setHost(null);
		assertFalse(m_instance.isService()); // null, theIdent, null
		m_instance.setNick(theNick);
		assertFalse(m_instance.isService()); // theNick, theIdent, null
		m_instance.setHost(theHost);
		assertFalse(m_instance.isService()); // theNick, theIdent, theHost
		m_instance.setIdent(null);
		assertTrue(m_instance.isService()); // theNick, null, theHost
		m_instance.setHost(null);
		assertFalse(m_instance.isService()); // theNick, null, null
		m_instance.setNick(null);
		assertFalse(m_instance.isService()); // null, null, null
	}

	@Test
	public void testIsOther() {
		assertTrue(m_instance.isOther()); // null, null, null
		m_instance.setHost(theHost);
		assertFalse(m_instance.isOther()); // null, null, theHost
		m_instance.setIdent(theIdent);
		assertTrue(m_instance.isOther()); // null, theIdent, theHost
		m_instance.setHost(null);
		assertTrue(m_instance.isOther()); // null, theIdent, null
		m_instance.setNick(theNick);
		assertTrue(m_instance.isOther()); // theNick, theIdent, null
		m_instance.setHost(theHost);
		assertFalse(m_instance.isOther()); // theNick, theIdent, theHost
		m_instance.setIdent(null);
		assertFalse(m_instance.isOther()); // theNick, null, theHost
		m_instance.setHost(null);
		assertTrue(m_instance.isOther()); // theNick, null, null
		m_instance.setNick(null);
		assertTrue(m_instance.isOther()); // null, null, null
	}

	@Test
	public void testWithPatternAtAndExcl() {
		Origin o = new Origin("mojadita!kania@te.llevo.en.mi.corazon");
		assertNotNull(o);
		assertEquals("mojadita", o.getNick());
		assertEquals("kania", o.getIdent());
		assertEquals("te.llevo.en.mi.corazon", o.getHost());
		assertTrue(o.isUser());
		assertFalse(o.isService());
		assertFalse(o.isServer());
		assertFalse(o.isOther());
	}

	@Test
	public void testWithPatternAt() {
		Origin o = new Origin("kania@te.llevo.en.mi.corazon");
		assertNotNull(o);
		assertEquals("kania", o.getNick());
		assertNull(o.getIdent());
		assertEquals("te.llevo.en.mi.corazon", o.getHost());
		assertFalse(o.isUser());
		assertTrue(o.isService());
		assertFalse(o.isServer());
		assertFalse(o.isOther());
	}

	@Test
	public void testWithPatternExcl() {
		Origin o = new Origin("mojadita!te.llevo.en.mi.corazon");
		assertNotNull(o);
		assertNull(o.getNick());
		assertNull(o.getIdent());
		assertEquals("mojadita!te.llevo.en.mi.corazon", o.getHost());
		assertFalse(o.isUser());
		assertFalse(o.isService());
		assertTrue(o.isServer());
		assertFalse(o.isOther());
	}

	@Test
	public void testWithPatternNone() {
		Origin o = new Origin("te.llevo.en.mi.corazon");
		assertNotNull(o);
		assertNull(o.getNick());
		assertNull(o.getIdent());
		assertEquals("te.llevo.en.mi.corazon", o.getHost());
		assertFalse(o.isUser());
		assertFalse(o.isService());
		assertTrue(o.isServer());
		assertFalse(o.isOther());
	}

	@Test
	public void testWithPatternReversed() {
		Origin o = new Origin("kania@mojadita!te.llevo.en.mi.corazon");
		assertNotNull(o);
		assertEquals("kania", o.getNick());
		assertNull(o.getIdent());
		assertEquals("mojadita!te.llevo.en.mi.corazon", o.getHost());
		assertFalse(o.isUser());
		assertTrue(o.isService());
		assertFalse(o.isServer());
		assertFalse(o.isOther());
	}

	@Test
	public void testToString() {
		assertEquals("<<UNKNOWN-null!null@null>>", m_instance.toString()); // null,
																			// null,
																			// null
		m_instance.setHost(theHost);
		assertEquals(theHost, m_instance.toString()); // null, null, theHost
		m_instance.setIdent(theIdent);
		assertEquals("<<UNKNOWN-null!" + theIdent + "@" + theHost + ">>", m_instance.toString()); // null,
																									// theIdent,
																									// theHost
		m_instance.setHost(null);
		assertEquals("<<UNKNOWN-null!" + theIdent + "@null>>", m_instance.toString()); // null,
																						// theIdent,
																						// null
		m_instance.setNick(theNick);
		assertEquals("<<UNKNOWN-" + theNick + "!" + theIdent + "@null>>", m_instance.toString()); // theNick,
																									// theIdent,
																									// null
		m_instance.setHost(theHost);
		assertEquals(theNick + "!" + theIdent + "@" + theHost, m_instance.toString()); // theNick,
																						// theIdent,
																						// theHost
		m_instance.setIdent(null);
		assertEquals(theNick + "@" + theHost, m_instance.toString()); // theNick,
																		// null,
																		// theHost
		m_instance.setHost(null);
		assertEquals("<<UNKNOWN-" + theNick + "!null@null>>", m_instance.toString()); // theNick,
																						// null,
																						// null
		m_instance.setNick(null);
		assertEquals("<<UNKNOWN-null!null@null>>", m_instance.toString()); // null,
																			// null,
																			// null
	}
}
