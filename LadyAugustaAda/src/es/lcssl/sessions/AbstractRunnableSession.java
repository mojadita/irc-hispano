/**
 * 
 */
package es.lcssl.sessions;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

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

	private BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_inputQueue;
	
	/**
	 * @param factory
	 * @param key
	 */
	protected AbstractRunnableSession(F factory, K key, Properties properties) {
		super(factory, key, properties);
		m_inputQueue = new LinkedBlockingQueue<Event<Monitor, IRCCode, IRCMessage>>();
	}

	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		m_inputQueue.add(event);
	}
	
	public BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> getInputQueue() {
		return m_inputQueue;
	}
}
