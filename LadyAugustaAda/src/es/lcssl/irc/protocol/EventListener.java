/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 3 de jun de 2016, 21:40
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

public interface EventListener<EG extends EventGenerator<EG, T, M>, T extends Enum<T>, M extends Message<T>> {
	
	void process(EG source, Event<EG, T, M> event);

}
