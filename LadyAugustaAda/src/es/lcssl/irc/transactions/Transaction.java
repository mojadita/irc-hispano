package es.lcssl.irc.transactions;

import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

public class Transaction implements EventListener<Monitor,IRCCode,IRCMessage> {
	
	private TransactionFactory m_factory;
	private IRCMessage         m_request;
	private Monitor	           m_monitor;
	private IRCMessage         m_response;
	private long			   m_timestamp;
	
	Transaction(TransactionFactory factory, IRCMessage request, Monitor monitor) {
		m_factory = factory;
		m_request = request;
		m_monitor = monitor;
		m_response = null;
		m_timestamp = System.currentTimeMillis();
	}
	
	void accept(IRCMessage response) {
		m_timestamp = System.currentTimeMillis();
		m_response = response;
	}

	@Override
	public void process(Monitor source,
			Event<Monitor, IRCCode, IRCMessage> event) {
		IRCMessage response = event.getMessage();
		// check if this is the final response we expect.
		if ( response.getCode() == m_request.getCode() &&
				response.getOrigin().getLowercaseNick().equals(m_monitor.getNick())
			 ) {
			accept(response);
			m_factory.unregisterTransaction(this);
			
		}
	}

	public IRCMessage getResponse() {
		return m_response;
	}

	public void setResponse(IRCMessage response) {
		m_response = response;
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

	public synchronized void start() {
	}
	
	public synchronized void end() {
		
	}
	
	public synchronized boolean isFinished() {
		return false;
	}
	
	public synchronized void waitToFinish() {
	}
	
	public synchronized void waitToFinish(long amount, TimeUnit units) {
	}

}
