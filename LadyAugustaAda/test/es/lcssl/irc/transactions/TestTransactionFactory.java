/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 19 de jun. de 2016 12:13:09.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.transactions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.transactions;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Iterator;

import static org.junit.Assert.*;

import org.easymock.MockType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.InputMonitor;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;
import es.lcssl.irc.transactions.TransactionFactory.Transaction;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
//@RunWith(EasyMockRunner.class)
public class TestTransactionFactory {
	
	TransactionFactory iut;
	IRCMessage req, reqPART, ansPART, ansOK, ansOTHER, ansERR, ansNOfinal, reqWHOIS, respWHOIS, respENDOFWHOIS;
	Transaction t;
	IrcSAP sap;
	IrcSAP.InputMonitor imon;
	IrcSAP.OutputMonitor omon;
	
	@Before
	public void beforeTests() {
		sap = createMock(MockType.STRICT, IrcSAP.class);
		imon = createMock(MockType.STRICT, IrcSAP.InputMonitor.class);
		omon = createMock(MockType.STRICT, IrcSAP.OutputMonitor.class);
		
		iut = new TransactionFactory(sap); // Instance Under Test.
		
		req = new IRCMessage(IRCCode.JOIN, "#channel", "ThisIsThePassword");
		reqPART = new IRCMessage(IRCCode.PART, "#channel2", "Goodbye");
		ansPART = new IRCMessage(new Origin("nick","user","host.com"), IRCCode.PART, "#channel2", "Goodbye");
		ansOK = new IRCMessage(new Origin("nick","user","host.com"), IRCCode.JOIN, "#channel");
		ansOTHER = new IRCMessage(new Origin("nock","user","host.com"), IRCCode.JOIN, "#channel");

		ansERR = new IRCMessage(new Origin("irc.server.fi"), 
				IRCCode.ERR_NEEDMOREPARAMS, "nick", "JOIN", "Need more parameters");
		reqWHOIS = new IRCMessage(IRCCode.WHOIS, "nick");
		respWHOIS = new IRCMessage(IRCCode.RPL_WHOISCHANNELS, "nick", "nick", "+#a @#b #c +@#d");
		respENDOFWHOIS = new IRCMessage(IRCCode.RPL_ENDOFWHOIS, "nick", "nick", "End of whois");
		ansNOfinal = new IRCMessage(new Origin("ariel.chathispano.com"), 
				IRCCode.RPL_TOPIC, 
				"AdaAugustaLovelace", 
				"#pegatumocoaqui", 
				"And go again for the beer!!!");
	}
	
	public void replayAll() {
		replay(sap, imon, omon);
	}

	@After
	public void verifyAll() {
		verify(sap, imon, omon);
	}
	
	@Test
	public void testInitialization() {
		assertNotNull(iut.m_irccodeRegistry);
		assertEquals(0, iut.m_irccodeRegistry.size());
		assertEquals(0, iut.m_nextId);
		assertSame(sap, iut.m_sap);
		replayAll();
	}
	
	@Test
	public void testNewTransactionInitialState() throws IllegalStateException, InterruptedException {
		
		iut = new TransactionFactory(sap);
		t = iut.newTransaction(req);
		assertNotNull(t);
		assertEquals(TransactionState.IDLE, t.getState());
		assertSame(iut, t.getFactory());
		assertEquals(0L, t.getId());
		assertSame(req, t.getRequest());
		assertNotNull(t.getEvents());
		assertTrue(t.getEvents().isEmpty());
		assertTrue(t.getResponses().isEmpty());
		replayAll();
	}
	
	@Test
	public void testProcessWithNoTransactionRegistered() {
		// we should not receive messages in case of no transaction registered, so 
		// we expect an imon.unregister() for the event that arrived.
		imon.unregister(IRCCode.JOIN, iut);
		replayAll();
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansOK));
	}

	private void testRegister(IRCCode code) {
		expect(sap.getInputMonitor()).andReturn(imon);
		imon.register(code, iut);
	}

	private void testUnregister(IRCCode code) {
		expect(sap.getInputMonitor()).andReturn(imon);
		imon.unregister(code, iut);
	}
	/**
	 * We must create a transaction, using a request message.
	 * Once a transaction is created, we start it, this makes
	 * {@link TransactionFactory} to call its configured {@link IrcSAP#getInputMonitor()} method
	 * to get the {@link InputMonitor} and register for incoming messages of the possible response
	 * types associated to this message code.
	 * @throws IllegalStateException it should not throw this exception.
	 */
	@Test
	public void testProcessWithOneTransactionRegistered() throws IllegalStateException {
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testRegister(c);
		testRegister(IRCCode.JOIN);
		sap.addMessage(req);
		
		
		replayAll(); // begin the tests.
		
		Transaction t = iut.newTransaction(req);
		assertNotNull(t);
		t.start();
		assertEquals(0, t.getId());
		assertSame(req, t.getRequest());
		assertEquals(0, t.getResponses().size());
		assertEquals(TransactionState.STARTED, t.getState());
	}
	
	@Test
	public void testProcessWithOneTransactionAndError() throws IllegalStateException {
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testRegister(c);
		testRegister(IRCCode.JOIN);
		sap.addMessage(req);
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testUnregister(c);
		testUnregister(IRCCode.JOIN);
		
		replayAll(); // begin the tests.
		
		Transaction t = iut.newTransaction(req);
		assertNotNull(t);
		t.start();
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansERR));
		assertEquals(TransactionState.ERROR, t.getState());
		assertEquals(1, t.getResponses().size());
		assertSame(ansERR, t.getResponses().iterator().next());
	}
	
	@Test
	public void testProcessWithOneTransactionAndOK() throws IllegalStateException {
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testRegister(c);
		testRegister(IRCCode.JOIN);
		sap.addMessage(req);
		expect(sap.getNick()).andReturn("NICK"); // as we receive a JOIN message, we have to check the nick.
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testUnregister(c);
		testUnregister(IRCCode.JOIN);
		
		replayAll(); // begin the tests.
		
		Transaction t = iut.newTransaction(req);
		assertNotNull(t);
		t.start();
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansOK));
		assertEquals(TransactionState.ENDED, t.getState());
		assertEquals(1, t.getResponses().size());
		assertSame(ansOK, t.getResponses().iterator().next());
	}
	
	@Test
	public void testProcessWithOneTransactionAndOKWithPreviousNotValid() throws IllegalStateException {
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testRegister(c);
		testRegister(IRCCode.JOIN);
		sap.addMessage(req);
		expect(sap.getNick()).andReturn("NICK"); // as we receive a JOIN message, we have to check the nick.
		expect(sap.getNick()).andReturn("NICK"); // as we receive a JOIN message, we have to check the nick.
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testUnregister(c);
		testUnregister(IRCCode.JOIN);
		
		replayAll(); // begin the tests.
		
		Transaction t = iut.newTransaction(req);
		assertNotNull(t);
		t.start();
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansOTHER));
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansOK));
		assertEquals(TransactionState.ENDED, t.getState());
		assertEquals(1, t.getResponses().size());
		assertSame(ansOK, t.getResponses().iterator().next());
	}
	
	@Test
	public void testProcessWithOneTransactionAndOKWithTwoResponses() throws IllegalStateException {
		for (IRCCode c: IRCCode.WHOIS.getResponses())
			testRegister(c);
		sap.addMessage(reqWHOIS);
		for (IRCCode c: IRCCode.WHOIS.getResponses())
			testUnregister(c);
		
		replayAll(); // begin the tests.
		
		Transaction t = iut.newTransaction(reqWHOIS);
		assertNotNull(t);
		t.start();
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, respWHOIS));
		assertEquals(TransactionState.STARTED, t.getState());
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, respWHOIS));
		assertEquals(TransactionState.STARTED, t.getState());
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, respENDOFWHOIS));
		assertEquals(TransactionState.ENDED, t.getState());
		assertEquals(3, t.getResponses().size());
		Iterator<IRCMessage> it = t.getResponses().iterator();
		assertSame(respWHOIS, it.next());
		assertSame(respWHOIS, it.next());
		assertSame(respENDOFWHOIS, it.next());
	}

	@Test
	public void testProcessingTwoTransactionsAndOkBoth() throws IllegalStateException {
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testRegister(c);
		testRegister(IRCCode.JOIN);
		sap.addMessage(req);
		testRegister(IRCCode.ERR_NOTONCHANNEL);
		testRegister(IRCCode.PART);
		sap.addMessage(reqPART);
		expect(sap.getNick()).andReturn("nick");		
		testUnregister(IRCCode.ERR_NOTONCHANNEL);
		testUnregister(IRCCode.PART);
		expect(sap.getNick()).andReturn("nick");
		for (IRCCode c: IRCCode.JOIN.getResponses())
			testUnregister(c);
		testUnregister(IRCCode.JOIN);
		
		replayAll(); // begin the tests.
		
		Transaction t1 = iut.newTransaction(req);
		assertNotNull(t1);
		assertEquals(TransactionState.IDLE, t1.getState());
		Transaction t2 = iut.newTransaction(reqPART);
		assertNotNull(t2);
		assertEquals(TransactionState.IDLE, t2.getState());
		t1.start();
		assertSame(TransactionState.STARTED, t1.getState());
		t2.start();
		assertSame(TransactionState.STARTED, t2.getState());
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansPART));
		iut.process(new Event<Monitor,IRCCode,IRCMessage>(imon, ansOK));
	}
}
