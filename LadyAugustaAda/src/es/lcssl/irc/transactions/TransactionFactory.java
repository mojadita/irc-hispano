/**
 * 
 */
package es.lcssl.irc.transactions;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * @author lcu
 *
 */
public class TransactionFactory {

	Monitor							 m_monitor;
	Map<IRCCode, Queue<Transaction>> m_irccodeRegistry;
	Queue<Transaction> 				 m_transactionQueue;
	
	public TransactionFactory(Monitor monitor) {
		m_irccodeRegistry = new EnumMap<IRCCode, Queue<Transaction>>(IRCCode.class);
		m_transactionQueue = new LinkedTransferQueue<Transaction>();
		m_monitor = monitor;
	}
	
	public Transaction newTransaction(IRCMessage message) {
		Transaction result =  new Transaction(this, message, m_monitor, m_monitor.getNick().toLowerCase());
		return result;
	}
	
	public Transaction selectTransaction(IRCCode code) {
		Queue<Transaction> theQueue = m_irccodeRegistry.get(code);
		if (theQueue == null) return null;
		return theQueue.poll();
	}
	
	void registerTransaction(Transaction transaction) {
		if (this != transaction.getFactory())
			throw new IllegalArgumentException(
					"I can only register transactions created by me.");
		IRCCode[] codes = transaction.getRequest().getCode().getResponses();
		// if no codes, no transaction registration.
		if (codes == null || codes.length == 0)
			return;
		synchronized (this) {
			for (IRCCode code: codes) {
				Queue<Transaction> queue = m_irccodeRegistry.get(code);
				if (queue == null) {
					queue = new LinkedTransferQueue<Transaction>();
					m_irccodeRegistry.put( code, queue );
				}
				queue.add(transaction);
			}
			m_transactionQueue.add(transaction);
		}
	}
	
	void unregisterTransaction(Transaction transaction) {
		IRCCode[] codes = transaction.getRequest().getCode().getResponses();
		// if no codes, no transaction has been registered.
		if (codes == null || codes.length == 0)
			return;

		synchronized (this) {
			Transaction top;
			while ((top = m_transactionQueue.poll()) != null && top != transaction) {
				for (IRCCode code: top.getRequest().getCode().getResponses()) {
				
			}
			for (IRCCode code: codes) {
				Queue<Transaction> queue = m_irccodeRegistry.get(code);
				if (queue == null || queue.size() == 0) continue;
				queue.remove(transaction);
				if (queue.isEmpty()) m_irccodeRegistry.remove(code);
			}
			Transaction top;
			while (top = m_transactionQueue.poll() != null && top != transaction) {
				
			}
		}
	}
	
}
