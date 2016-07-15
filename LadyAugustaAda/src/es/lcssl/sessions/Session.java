/**
 * 
 */
package es.lcssl.sessions;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * A {@link Session} is a {@link Thread} with some specific inteface to be able
 * to get {@link Event}s from the {@link SessionManager} and process them.
 * <p>
 * {@link Session}s must implement a {@link #run(SessionManager)} method which
 * is run in an independent {@link Thread}. The {@link SessionManager} acts as a
 * demultiplexor of incoming events and dispatches them to the proper
 * {@link Session} instance by means of its input queue. The instances must call
 * {@link SessionManager#getEvent()} or
 * {@link SessionManager#getEvent(long, java.util.concurrent.TimeUnit)} methods,
 * to get access to the input {@link Event}s.
 * 
 * @author lcu
 *
 */
public interface Session<
		F extends Factory<F, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<F, K, S>>
{

	/**
	 * Getter for the {@code K} {@code key} attribute.
	 * @return the {@code K key} attribute for this {@link Session}.
	 */
	K getKey();

	/**
	 * Getter for the {@code F} {@code factory} attribute.
	 * @return the reference to the {@link Factory} that created this {@link Session}.
	 */
	F getFactory();

	/**
	 * Method to process events comming from the Monitor event generator.
	 * @param event
	 */
	void process(Event<Monitor, IRCCode, IRCMessage> event);

}
