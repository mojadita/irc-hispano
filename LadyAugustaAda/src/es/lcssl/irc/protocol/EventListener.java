package es.lcssl.irc.protocol;

public interface EventListener<EG extends EventGenerator<EG, T, M>, T extends Enum<T>, M extends Message<T>> {
	
	void process(EG source, Event<EG, T, M> event);

}
