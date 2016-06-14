/**
 * 
 */
package es.lcssl.irc.transactions;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * @author lcu
 *
 */
public class TransactionFactory {

	private long m_nextId;
	
	Map<IRCCode, Queue<Transaction>> m_irccodeRegistry;
	
	public Transaction newTransaction(IRCMessage message, Monitor monitor) {
		return null;
	}
	
	public Transaction selectTransaction(IRCCode code) {
		Queue<Transaction> theQueue = m_irccodeRegistry.get(code);
		if (theQueue == null) return null;
		return theQueue.poll();
	}
	
	void registerTransaction(Transaction transaction) {
		Queue<Transaction> theQueue = transaction.getRequest().getCode().
	}
	
}
