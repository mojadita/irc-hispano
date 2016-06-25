/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 25 de jun. de 2016 12:21:09.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.access
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class TestAdminsTableFactory {
	
	AdminsTableFactory<Integer> iut;
	AdminsTableFactory<Integer>.AdminsTable set1, set2, set3, set4;
	
	@Before
	public void beforeTest() {
		iut = new AdminsTableFactory<Integer>();
		set1 = iut.lookup("root");
		set2 = iut.lookup(set1, "services");
		set3 = iut.lookup(set1, "services2");
		set4 = iut.lookup(set3, "subservices3");
		set1.add(0);
		set2.add(1);
		set2.add(3);
		set3.add(4);
		set3.add(5);
		set4.add(7);
		set4.add(8);
	}
	
	@Test
	public void testBasicAccess() {
		assertNotNull(set1);
		assertEquals("root", set1.getName());
		assertNull(set1.getParent());
		
		assertNotNull(set2);
		assertEquals("services", set2.getName());
		assertSame(set1, set2.getParent());
		
		assertNotNull(set3);
		assertEquals("services2", set3.getName());
		assertSame(set1, set3.getParent());
		
		assertNotNull(set4);
		assertEquals("subservices3", set4.getName());
		assertSame(set3, set4.getParent());
	}
	
	@Test
	public void testAccessToService() {
		assertTrue(set1.checkAccessToThis(0));
		assertTrue(set1.checkAccessToResources(0));
		assertTrue(set2.checkAccessToThis(0));
		assertTrue(set2.checkAccessToResources(0));
		assertTrue(set3.checkAccessToThis(0));
		assertTrue(set3.checkAccessToResources(0));
		assertTrue(set4.checkAccessToThis(0));
		assertTrue(set4.checkAccessToResources(0));
		
		assertFalse(set1.checkAccessToThis(1));
		assertFalse(set1.checkAccessToResources(1));
		assertFalse(set2.checkAccessToThis(1));
		assertTrue(set2.checkAccessToResources(1));
		assertFalse(set3.checkAccessToThis(1));
		assertFalse(set3.checkAccessToResources(1));
		assertFalse(set4.checkAccessToThis(1));
		assertFalse(set4.checkAccessToResources(1));
		
		assertFalse(set1.checkAccessToThis(2));
		assertFalse(set1.checkAccessToResources(2));
		assertFalse(set2.checkAccessToThis(2));
		assertFalse(set2.checkAccessToResources(2));
		assertFalse(set3.checkAccessToThis(2));
		assertFalse(set3.checkAccessToResources(2));
		assertFalse(set4.checkAccessToThis(2));
		assertFalse(set4.checkAccessToResources(2));
		
		assertFalse(set1.checkAccessToThis(3));
		assertFalse(set1.checkAccessToResources(3));
		assertFalse(set2.checkAccessToThis(3));
		assertTrue(set2.checkAccessToResources(3));
		assertFalse(set3.checkAccessToThis(3));
		assertFalse(set3.checkAccessToResources(3));
		assertFalse(set4.checkAccessToThis(3));
		assertFalse(set4.checkAccessToResources(3));
		
		assertFalse(set1.checkAccessToThis(4));
		assertFalse(set1.checkAccessToResources(4));
		assertFalse(set2.checkAccessToThis(4));
		assertFalse(set2.checkAccessToResources(4));
		assertFalse(set3.checkAccessToThis(4));
		assertTrue(set3.checkAccessToResources(4));
		assertTrue(set4.checkAccessToThis(4));
		assertTrue(set4.checkAccessToResources(4));
		
		assertFalse(set1.checkAccessToThis(5));
		assertFalse(set1.checkAccessToResources(5));
		assertFalse(set2.checkAccessToThis(5));
		assertFalse(set2.checkAccessToResources(5));
		assertFalse(set3.checkAccessToThis(5));
		assertTrue(set3.checkAccessToResources(5));
		assertTrue(set4.checkAccessToThis(5));
		assertTrue(set4.checkAccessToResources(5));
		
		assertFalse(set1.checkAccessToThis(6));
		assertFalse(set1.checkAccessToResources(6));
		assertFalse(set2.checkAccessToThis(6));
		assertFalse(set2.checkAccessToResources(6));
		assertFalse(set3.checkAccessToThis(6));
		assertFalse(set3.checkAccessToResources(6));
		assertFalse(set4.checkAccessToThis(6));
		assertFalse(set4.checkAccessToResources(6));
		
		assertFalse(set1.checkAccessToThis(7));
		assertFalse(set1.checkAccessToResources(7));
		assertFalse(set2.checkAccessToThis(7));
		assertFalse(set2.checkAccessToResources(7));
		assertFalse(set3.checkAccessToThis(7));
		assertFalse(set3.checkAccessToResources(7));
		assertFalse(set4.checkAccessToThis(7));
		assertTrue(set4.checkAccessToResources(7));
		
		assertFalse(set1.checkAccessToThis(8));
		assertFalse(set1.checkAccessToResources(8));
		assertFalse(set2.checkAccessToThis(8));
		assertFalse(set2.checkAccessToResources(8));
		assertFalse(set3.checkAccessToThis(8));
		assertFalse(set3.checkAccessToResources(8));
		assertFalse(set4.checkAccessToThis(8));
		assertTrue(set4.checkAccessToResources(8));
		
		assertFalse(set1.checkAccessToThis(9));
		assertFalse(set1.checkAccessToResources(9));
		assertFalse(set2.checkAccessToThis(9));
		assertFalse(set2.checkAccessToResources(9));
		assertFalse(set3.checkAccessToThis(9));
		assertFalse(set3.checkAccessToResources(9));
		assertFalse(set4.checkAccessToThis(9));
		assertFalse(set4.checkAccessToResources(9));
	}

}
