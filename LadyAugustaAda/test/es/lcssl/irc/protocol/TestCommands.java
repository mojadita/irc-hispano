/**
 * 
 */
package es.lcssl.irc.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author luis
 *
 */
public class TestCommands {
	@Test
	public void testNumberOfCommands() {
		assertEquals(40, Command.values().length);
		for (Command c: Command.values()) {
			assertNotNull(c.getResponses());
		}
	}

}
