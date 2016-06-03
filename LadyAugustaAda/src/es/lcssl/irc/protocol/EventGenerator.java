package es.lcssl.irc.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EventGenerator<
	EG extends EventGenerator<EG, T, M>, 
	T extends Enum<T>, 
	M extends Message<T>>
extends Thread
{
	
	private Map<T, Collection<EventListener<EG, T, M>>> m_map;
	Collection<EventListener<EG, T, M>> m_unbound;
	
	public EventGenerator(Class<T> cl) {
		m_map = new EnumMap<T, Collection<EventListener<EG, T, M>>>(cl);
		m_unbound = new ArrayList<EventListener<EG, T, M>>();
	}
	
	public synchronized void register(T type, EventListener<EG, T, M> listener) {
		Collection<EventListener<EG, T, M>> coll = m_map.get(type);
		if (coll == null) {
			m_map.put(type, coll = new ArrayList<EventListener<EG, T, M>>());
		}
		coll.add(listener);
	}
	
	public synchronized void register(EventListener<EG, T, M> listener) {
		m_unbound.add(listener);
	}
	
	public synchronized void unregister(T type, EventListener<EG, T, M> listener) {
		Collection<EventListener<EG, T, M>> coll = m_map.get(type);
		if (coll == null) return;
		if (coll.remove(listener) && coll.size() == 0) {
			m_map.remove(type);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void fireEvent(long timestamp, M message) {
		List<EventListener<EG, T, M>> to_execute = new ArrayList<EventListener<EG, T, M>>();
		synchronized (this) {
			// we first collect the bound EventListeners...
			Collection<EventListener<EG, T, M>> bound = m_map.get(message.getCode());
			if (bound != null) to_execute.addAll(bound);
			// then the list of unbound EventListeners
			to_execute.addAll(m_unbound);
		}
		for(EventListener<EG, T, M> e: to_execute)
			e.process((EG) this, new Event<EG, T, M>(timestamp, (EG) this, message));
	}
}
