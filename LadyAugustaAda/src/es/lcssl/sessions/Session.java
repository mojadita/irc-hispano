/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 27 de jun. de 2016 21:07:48.
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
public interface Session<
		SF extends SessionFactory<SF, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<SF, K, S>> 
{
	K getKey();
	SF getSessionFactory();
}
