/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 19 de may. de 2016, 18:20:09
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

/**
 * Interface shared between RequestCode and ResponseCode.  This
 * interface allows to store both types of codes in 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public interface MessageCode {
	/**
	 * Method to TODO
	 * @return
	 */
	boolean isRequestCode();
	boolean isResponseCode();
}
