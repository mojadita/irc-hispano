/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 27 de jun. de 2016 21:04:36.
 * Project: LadyAugustaAda
 * Package: es.lcssl.sessions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.sessions;

import java.util.Collection;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public interface SessionFactory<
		SF extends SessionFactory<SF, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<SF, K, S>> 
{
	S lookup(K sessionKey);
	void cancel(K sessionKey);
	Collection<S> getSessions();
}
