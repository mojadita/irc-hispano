/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 19 de may. de 2016, 21:40:52
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

/**
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class TestIRCParser {
	
	InputStream in = new ByteArrayInputStream(
			(":a!b@c.d \t   PING        \t929123\r\n"
			+ ":deep.space  MODE          #literatura   +o    kania\r\n").getBytes());
	IRCParser iut;
	
	@Test
	public void testScan() {
		iut = new IRCParser(in);
		assertNotNull(iut);
		BasicMessage m = iut.scan();
		assertNotNull(m);
		assertEquals("a!b@c.d", m.getOrigin().toString());
		assertEquals("PING", m.getCode().getName());
		assertArrayEquals(new String[] {"929123"}, m.getParams().toArray());
		m = iut.scan();
		assertNotNull(m);
		assertEquals("deep.space", m.getOrigin().toString());
		assertEquals("MODE", m.getCode().getName());
		assertArrayEquals(
				new String[] {"#literatura", "+o", "kania"}, 
				m.getParams().toArray());
	}

}
