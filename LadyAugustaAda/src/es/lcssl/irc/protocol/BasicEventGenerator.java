/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 3 de Jun. de 2016, 21:39
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a working implementation of the {@link EventGenerator}
 * interface.
 * 
 * @param <EG>
 *            is the subclass implementing the interface.
 * @param <K>
 *            is the enum type uses as key for event type selection.
 * @param <M>
 *            is the message to act on receiving to fire all the
 *            {@link EventListener}s related to the key incoming in the received
 *            message.
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class BasicEventGenerator<
		EG extends EventGenerator<EG, K, M>, 
		K extends Enum<K>, 
		M extends Message<K>>
extends Thread 
implements TypedEventRegistrar<EG,K,M>, UntypedEventRegistrar<EG,K,M>
{
	
	private Map<K, Collection<EventListener<EG, K, M>>> m_map;
	Collection<EventListener<EG, K, M>> m_unbound;
	
	public BasicEventGenerator(Class<K> cl) {
		m_map = new EnumMap<K, Collection<EventListener<EG, K, M>>>(cl);
		m_unbound = new ArrayList<EventListener<EG, K, M>>();
	}
	
	/**
	 * @param type
	 * @param listener
	 * @see es.lcssl.irc.protocol.EventGenerator#register(T, es.lcssl.irc.protocol.EventListener)
	 */
	@Override
	public synchronized void register(K type, EventListener<EG, K, M> listener) {
		Collection<EventListener<EG, K, M>> coll = m_map.get(type);
		if (coll == null) {
			m_map.put(type, coll = new ArrayList<EventListener<EG, K, M>>());
		}
		coll.add(listener);
	}
	
	/**
	 * @param listener
	 * @see es.lcssl.irc.protocol.EventGenerator#register(es.lcssl.irc.protocol.EventListener)
	 */
	@Override
	public synchronized void register(EventListener<EG, K, M> listener) {
		m_unbound.add(listener);
	}
	
	/**
	 * @param type
	 * @param listener
	 * @see es.lcssl.irc.protocol.EventGenerator#unregister(T, es.lcssl.irc.protocol.EventListener)
	 */
	@Override
	public synchronized void unregister(K type, EventListener<EG, K, M> listener) {
		Collection<EventListener<EG, K, M>> coll = m_map.get(type);
		if (coll == null) return;
		if (coll.remove(listener) && coll.size() == 0) {
			m_map.remove(type);
		}
	}
	
	/**
	 * @param type
	 * @param listener
	 * @see es.lcssl.irc.protocol.EventGenerator#unregister(T, es.lcssl.irc.protocol.EventListener)
	 */
	@Override
	public synchronized void unregister(EventListener<EG, K, M> listener) {
		m_unbound.remove(listener);
	}

	/**
	 * @param timestamp
	 * @param message
	 * @see es.lcssl.irc.protocol.EventGenerator#fireEvent(long, M)
	 */
	public void fireEvent(Event<EG, K, M> event) {
		K key = event.getMessage().getCode();
		List<EventListener<EG, K, M>> to_execute = new ArrayList<EventListener<EG, K, M>>();
		synchronized (this) {
			// we first collect the bound EventListeners...
			Collection<EventListener<EG, K, M>> bound = m_map.get(key);
			if (bound != null) to_execute.addAll(bound);
			// then the list of unbound EventListeners
			to_execute.addAll(m_unbound);
		}
		for(EventListener<EG, K, M> e: to_execute)
			e.process(event);
	}
}
