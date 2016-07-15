/**
 * 
 */
package es.lcssl.sessions;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;

/**
 * @author lcu
 *
 */
public abstract class AbstractRunnableSession<
		F extends Factory<F, K, S>, 
		K extends Comparable<? super K>, 
		S extends AbstractRunnableSession<F, K, S>>
extends AbstractSession<F, K, S>
implements Runnable 
{

	protected BlockingQueue<Event<F, IRCCode, IRCMessage>> m_inputQueue;
	
	/**
	 * @param factory
	 * @param key
	 */
	protected AbstractRunnableSession(F factory, K key) {
		super(factory, key);
		m_inputQueue = new LinkedBlockingQueue<Event<F, IRCCode, IRCMessage>>();
	}

	@Override
	public void process(Event<F, IRCCode, IRCMessage> event) {
		m_inputQueue.add(event);
	}
	
	public BlockingQueue<Event<F, IRCCode, IRCMessage>> getInputQueue() {
		return m_inputQueue;
	}
}
