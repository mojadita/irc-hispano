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
 * {@link EventListener#process(Event)} method for any or all
 * the {@link EventListener}s registered.
 * 
 * @param <EG>
 *            This represents the exact class implementing this interface. This
 *            allows to pass the full type to the {@link EventListener}
 *            implementation and to access private fields of it.
 * @param <K>
 *            This is the enumeration that discriminates each message.
 * @param <M>
 *            This is the type of message that is fired upon arrival.
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public interface EventGenerator<EG extends EventGenerator<EG, K, M>, K extends Enum<K>, M extends Message<K>> {

}