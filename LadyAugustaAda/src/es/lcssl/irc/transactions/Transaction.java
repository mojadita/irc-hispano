/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 2016-06-14 09:03:00
 * Project: LadyAugustaAda
 * Package: el.lcssl.irc.transactions
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.transactions;

import java.util.ArrayList;
import java.util.Collection;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

public class Transaction implements EventListener<Monitor,IRCCode,IRCMessage> {
	
	private TransactionFactory                            m_factory;
	private IRCMessage                                    m_request;
	private Monitor	                                      m_monitor;
	private String										  m_nickLowercase;
	private Collection<Event<Monitor,IRCCode,IRCMessage>> m_events;
	private long			                              m_timestamp;
	private State			                              m_state;
	
	enum State {
		IDLE,  // Transaction has not yet begun.
		STARTED, // Transaction has started but not yet finished.
		FINISHED, // Transaction started and finished.
	}
	
	Transaction(TransactionFactory factory, IRCMessage request, Monitor monitor, String nickLowercase) {
		m_factory       = factory;
		m_request       = request;
		m_monitor       = monitor;
		m_nickLowercase = nickLowercase;
		m_events	    = new ArrayList<Event<Monitor,IRCCode,IRCMessage>>();
		m_timestamp     = System.currentTimeMillis();
		m_state         = State.IDLE;
	}
	
	@Override
	public void process(Monitor source,
			Event<Monitor, IRCCode, IRCMessage> event) {
		IRCMessage response = event.getMessage();
		Origin origin = response.getOrigin();
		IRCCode code = response.getCode();
		if (code == m_request.getCode() && origin.getLowercaseNick().equals(m_nickLowercase)) {
			// this is a response to our command, transaction finishes here.
		} else if (code.isError()) {
			// this is an error to our command, transaction finishes here.
		} else if (code.isReply()) {
			// this is a reply, but it can be final or not, check.
			if (code.isFinal()) {
				// reply is final, we have to finish transaction.
			} else {
				// reply is not final, we have to add it and wait for more.
			}
		} // else ignore it.
	}

	public IRCMessage getRequest() {
		return m_request;
	}

	public Monitor getMonitor() {
		return m_monitor;
	}
	
	public TransactionFactory getFactory() {
		return m_factory;
	}

	/**
	 * @return the nickLowercase
	 */
	public String getNickLowercase() {
		return m_nickLowercase;
	}

	/**
	 * @return the events
	 */
	public Collection<Event<Monitor, IRCCode, IRCMessage>> getEvents() {
		return m_events;
	}
	
	public Collection<IRCMessage> getResponses() {
		Collection<IRCMessage> responses = new ArrayList<IRCMessage>();
		for(Event<Monitor, IRCCode, IRCMessage> ev: m_events)
			responses.add(ev.getMessage());
		return responses;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return m_timestamp;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return m_state;
	}

	public synchronized void start() {
		// first, install all handlers for every possible response to this command.
		// then, send the command.
	}
	
	public synchronized void end() {
		// uninstall all handlers for every possible response to this command.
		// then, mark transaction as finished and wakeup all handlers waiting for
		// it to finish.
	}
	
	public void execute() {
		// start() and wait until isFinished() returns true.
	}
	
	public void execute(long timeout) {
		// record timestamp, add timeout and then
		// start() and wait until isFinished() returns true
		// or System.currenttimemillis() is larger than timestamp.
		
	}
	
	public synchronized boolean isFinished() {
		return m_state == State.FINISHED;
	}
	
	public synchronized void waitToFinish() throws InterruptedException {
		while (!isFinished()) wait();
	}
	
	public synchronized void waitToFinish(long amount) {
	}
}
