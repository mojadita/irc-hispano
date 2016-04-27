/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 23 de abr. de 2016, 17:23:12
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.ws.soap.AddressingFeature.Responses;

import org.junit.Test;

/**
 * Tests for the Response enumeration.
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class TestResponse {

	@Test
	public void testAscendingOrder() {
		int initial = 0;
		for (ResponseCode r : ResponseCode.values()) {
			assertTrue(r + " is not in ascending order", initial < r.getProtocolValue());
			initial = r.getProtocolValue();
		}
	}

	@Test
	public void testAllHaveDescrip() {
		for (ResponseCode r : ResponseCode.values()) {
			int pv = r.getProtocolValue();
			assertEquals(String.format("%03d", pv), r.getStringValue());
		}
	}

	@Test
	public void testAllResponsesAppearInOneCommand() {
		for (ResponseCode r : ResponseCode.values()) {
			boolean appears = false;
			outer: for (RequestCode c : RequestCode.values()) {
				ResponseCode[] resps = c.getResponses();
				for (int i = 0; i < resps.length; i++) {
					if (r == resps[i]) {
						appears = true;
						break outer;
					}
				}
			}
			assertTrue("Response " + r + " appears in no command as response.", appears);
		}
	}

}
