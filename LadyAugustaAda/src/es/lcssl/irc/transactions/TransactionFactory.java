/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 18 de jun. de 2016 10:00.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.transactions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.transactions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventGenerator;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

/**
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 *
 */
public class TransactionFactory 
implements 
		EventListener<Monitor, IRCCode, IRCMessage>, 
		EventGenerator<TransactionFactory, IRCCode, IRCMessage> 
{

	long							m_nextId;
	IrcSAP							m_sap;
	Map<IRCCode, List<Transaction>> m_irccodeRegistry;

	public TransactionFactory(IrcSAP sap) {
		m_nextId = 0;
		m_sap = sap;
		m_irccodeRegistry = new EnumMap<IRCCode, List<Transaction>>(IRCCode.class);
		System.out.println("new " + this);
	}

	public class Transaction 
	implements EventListener<TransactionFactory, IRCCode, IRCMessage> 
	{

		private long			 m_id;
		private IRCMessage       m_request; // the request.
		private Collection<Event<TransactionFactory,IRCCode,IRCMessage>>
							     m_events;  // the collection of responses received.
		private TransactionState m_state;

		Transaction(IRCMessage request) {
			synchronized(TransactionFactory.this) {
				m_id = m_nextId++;
			}
			System.out.println("new " + this);
			m_request       = request;
			m_events	    = new ArrayList<Event<TransactionFactory,IRCCode,IRCMessage>>();
			m_state         = TransactionState.IDLE;
		}

		/**
		 * Getter for the {@code long} {@code id} attribute.
		 * @return the value of the {@code id}.
		 */
		public long getId() {
			return m_id;
		}

		public IRCMessage getRequest() {
			return m_request;
		}

		public TransactionFactory getFactory() {
			return TransactionFactory.this;
		}

		public Collection<Event<TransactionFactory, IRCCode, IRCMessage>> getEvents() {
			return m_events;
		}

		public Collection<IRCMessage> getResponses() {
			Collection<IRCMessage> responses = new ArrayList<IRCMessage>();
			for(Event<TransactionFactory, IRCCode, IRCMessage> ev: m_events)
				responses.add(ev.getMessage());
			return responses;
		}

		public TransactionState getState() {
			return m_state;
		}

		public void start() throws IllegalStateException {
			// first, install all handlers for every possible response to this command.
			synchronized (m_irccodeRegistry) {
				switch (m_state) {
				case IDLE:
					System.out.println("start " + this);
					m_state = TransactionState.STARTED;
					register(this);
					// then, send the command.
					m_sap.addMessage(m_request);
					break;
				default:
					throw new IllegalStateException(
							this + " is in illegal state " 
							+ m_state + " to be started");
				} // switch
			} // synchronized
		} // start()

		@Override
		public void process(Event<TransactionFactory,IRCCode,IRCMessage> event) 
		{
			synchronized (m_irccodeRegistry) {
				IRCMessage response = event.getMessage();
				Origin origin = response.getOrigin();
				IRCCode code = response.getCode();
				if (   code == m_request.getCode() 
					&& origin.getNick().equalsIgnoreCase(m_sap.getNick())) 
				{
					m_events.add(event);
					unregister(this);
					m_state = TransactionState.ENDED;
					m_irccodeRegistry.notifyAll();
				} else if (code.isError()) {
					m_events.add(event);
					unregister(this);
					m_state = TransactionState.ERROR;
					m_irccodeRegistry.notifyAll();
				} else if (code.isReply()) {
					m_events.add(event);
					if (code.isFinal()) {
						// reply is final, we have to finish transaction.
						unregister(this);
						m_state = TransactionState.ENDED;
						m_irccodeRegistry.notifyAll();
					}
				} // else ignore it.
			}
		}

		public void execute() throws InterruptedException, IllegalStateException {
			start();
			waitToFinish();
		}

		public void execute(long timeout) throws InterruptedException, IllegalStateException {
			start();
			waitToFinish(timeout);
		}

		public boolean isFinished() {
			synchronized (m_irccodeRegistry) {
				return m_state.isFinal();
			}
		}

		public void waitToFinish() throws InterruptedException {
			synchronized (m_irccodeRegistry) {
				while (!isFinished()) m_irccodeRegistry.wait();
			}
		}

		public synchronized void waitToFinish(long timeout) throws InterruptedException {
			long limit = System.currentTimeMillis() + timeout;
			synchronized (m_irccodeRegistry) {
				long timeto;
				while (!isFinished() && (timeto = limit - System.currentTimeMillis()) > 0) {
					m_irccodeRegistry.wait(timeto);
				}
			}
		}
		
		@Override
		public String toString() {
			return TransactionFactory.this.toString() + "[" + getId() + "]";
		}
	} // Transaction

	public Transaction newTransaction(IRCMessage message) {
		Transaction result =  new Transaction(message);
		return result;
	}

	/**
	 * @param type
	 * @param listener
	 * @see es.lcssl.irc.protocol.EventGenerator#register(java.lang.Enum, es.lcssl.irc.protocol.EventListener)
	 */
	private void register(Transaction listener) {
		synchronized (m_irccodeRegistry) {
			IRCCode[] responses = listener.getRequest().getCode().getResponses();
			if (responses != null) {
				System.out.println("  register " + listener);
				for (IRCCode resp: responses) {
					System.out.println("  code = " + resp);
					List<Transaction> list = m_irccodeRegistry.get(resp);
					if (list == null) {
						System.out.println("adding handler for " + resp);
						list = new ArrayList<Transaction>(4);
						m_irccodeRegistry.put(resp, list);
						m_sap.getInputMonitor().register(resp, TransactionFactory.this);
					}
					list.add((Transaction) listener);
				} // for
			}
		} // synchronized
	} // register(Transaction)

	/**
	 * @param type
	 * @param listener
	 * @see es.lcssl.irc.protocol.EventGenerator#unregister(java.lang.Enum, es.lcssl.irc.protocol.EventListener)
	 */
	private void unregister(Transaction listener) {
		synchronized (m_irccodeRegistry) {
			IRCCode[] codes = listener.getRequest().getCode().getResponses();
			System.out.println("  unregister " + listener);
			for (IRCCode resp : codes) {
				System.out.println("  code = " + resp);
				List<Transaction> list = m_irccodeRegistry.get(resp);
				if (list != null && list.size() > 0 && list.remove(listener)) {
					System.out.println("  deleting handler for " + resp);
					m_irccodeRegistry.remove(resp);
					m_sap.getInputMonitor().unregister(resp, this); // we don't use it more.
				} // if
			} // for
		} // synchronized
	} // unregister(Transaction)

	/**
	 * @param source
	 * @param event
	 * @see es.lcssl.irc.protocol.EventListener#process(es.lcssl.irc.protocol.Event)
	 */
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		synchronized (m_irccodeRegistry) {
			long timestamp = event.getTimestamp();
			Monitor monitor = event.getSource();
			IRCMessage message = event.getMessage();
			IRCCode code = message.getCode();
			Event<TransactionFactory,IRCCode,IRCMessage> eventDown =
					new Event<TransactionFactory,IRCCode,IRCMessage>(timestamp, this, message);
			List<Transaction> list = m_irccodeRegistry.get(code);
			if (list != null) { // get the head of the list and process it.
				list.get(0).process(eventDown);
			} else {
				System.out.println("WARNING: spurious " + code + " message received at " + this + ", ignored");
				// unregister to not receive more messages of this kind.
				monitor.unregister(code, this);
			} // if/else
		} // synchronized
	} // process(Event<Monitor,IRCCode,IRCMessage>)
} // TransactionFactory
