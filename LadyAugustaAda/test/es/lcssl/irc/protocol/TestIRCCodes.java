/**
 * 
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author luis
 *
 */
public class TestIRCCodes {
	
	@Test
	public void testNumberOfCommands() {
		int lastCode = -1;
		for (IRCCode c: IRCCode.values()) {
			if (c.isRequest()) {
				assertEquals(c.toString(), c.name(), c.getName());
				assertFalse(c.toString(), c.isReply());
				assertFalse(c.toString(), c.isError());
				assertEquals(c.toString(), -1, c.getCode());
				assertEquals(c.toString(), c.name(), c.getName());
			} else {
				assertTrue(c.toString(), c.isReply());
				int code = c.getCode();
				if (lastCode >= code) 
					fail(c + " fails order assertion with code " + code);
				lastCode = code;
				assertEquals(c + " fails getName() assertion", 
						String.format("%03d", code), 
						c.getName());
				if (c.isError()) {
					assertTrue(c.toString(), c.toString().matches("ERR_.*"));
				} else {
					assertTrue(c.toString(), c.toString().matches("RPL_.*"));
				}
			}
		}
		
	}

}
