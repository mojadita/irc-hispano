/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 19 de jun. de 2016 12:13:09.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.transactions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.transactions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.transactions.TransactionFactory.Transaction;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class TestTransactionFactory {
	
	TransactionFactory iut;
	IRCMessage m;
	Transaction t;
	
	@Before
	public void beforeTests() {
		iut = new TransactionFactory(null);
		m = new IRCMessage(IRCCode.JOIN, "#channel", "ThisIsThePassword");
	}

	@Test
	public void testInitialization() {
		assertNotNull(iut.m_irccodeRegistry);
		assertEquals(0, iut.m_irccodeRegistry.size());
		assertEquals(0, iut.m_nextId);
		assertNull(iut.m_sap);
	}
	
	@Test
	public void testNewTransaction() {
		iut = new TransactionFactory(null);
		t = iut.newTransaction(m);
		assertNotNull(t);
	}
	
	

}
