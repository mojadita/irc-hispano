/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 19 de may. de 2016, 21:40:52
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

/**
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class TestIRCParser {
	
	InputStream in = new ByteArrayInputStream(
			(":a!b@c.d PING 929123\r\n"
			+ ":deep.space MODE #literatura +o kania\r\n").getBytes());
	
	@Test
	public void testConstructor() {
		IRCParser iut = new IRCParser(in);
		assertNotNull(iut);
		
	}

}
