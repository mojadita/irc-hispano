package es.lcssl.irc.transactions;

import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

public class Transaction implements EventListener<Monitor,IRCCode,IRCMessage> {
	
	TransactionFactory m_factory;
	long	   		   m_id;
	IRCMessage         m_request;
	Monitor	           m_monitor;
	IRCMessage         m_response;
	
	Transaction(TransactionFactory factory, long id, IRCMessage request, Monitor monitor) {
		m_factory = factory;
		m_id = id;
		m_request = request;
		m_monitor = monitor;
		m_response = null;
	}

	@Override
	public void process(Monitor source,
			Event<Monitor, IRCCode, IRCMessage> event) {
		// TODO Auto-generated method stub
		
	}

	public IRCMessage getResponse() {
		return m_response;
	}

	public void setResponse(IRCMessage response) {
		m_response = response;
	}

	public long getId() {
		return m_id;
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
