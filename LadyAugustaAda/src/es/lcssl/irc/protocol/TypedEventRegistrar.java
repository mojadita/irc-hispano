/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 18 de jun. de 2016 12:09:27.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

/**
 * This interface represents a generic event generator. Events are diferentiated
 * by a key {@code K} which arrives on each message {@code M}.
 * {@link EventListener}s can be registered to be called on each message of
 * single key or for any key. The arrival of a message is signalled by the
 * {@link #fireEvent(long, Message)} method, which makes a call to the
 * {@link EventListener#process(Event)} method for any or all the
 * {@link EventListener}s registered.
 * 
 * @param <EG>
 *            This represents the exact class implementing this interface. This
 *            allows to pass the full type to the {@link EventListener}
 *            implementation and to access private fields of it.
 * @param <K>
 *            is the key type to classify messages.
 * @param <M>
 *            This is the type of message that is fired upon arrival.
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public interface TypedEventRegistrar<
		EG extends EventGenerator<EG,K,M>, 
		K extends Enum<K>, 
		M extends Message<K>> extends EventGenerator<EG,K,M> {

	/**
	 * Method to register a specific event for an {@link EventListener}. This
	 * means that after this call, the {@link EventListener}s registered on this
	 * type of event will be called to process the message.
	 * 
	 * @param type
	 *            the event type to register this {@link EventListener} to.
	 * @param listener
	 *            the {@link EventListener} to be registered for this type of
	 *            messages.
	 */
	void register(K type, EventListener<EG, K, M> listener);

	/**
	 * Method to unregister a specific {@link EventListener} for messages of
	 * type {@code type}.
	 * 
	 * @param type
	 *            the type of message this {@link EventListener} is registered
	 *            on.
	 * @param listener
	 *            the {@link EventListener} instance.
	 */
	void unregister(K type, EventListener<EG, K, M> listener);

}