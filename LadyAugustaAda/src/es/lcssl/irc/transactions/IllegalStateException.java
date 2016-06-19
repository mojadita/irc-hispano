/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 19 de jun. de 2016 18:24:32.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.transactions
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.transactions;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class IllegalStateException extends TransactionException {

	private static final long serialVersionUID = 1410601296170489376L;

	/**
	 * Constructs a new exception with the specified detail {@code message}. The
	 * {@code cause} is not initialized, and may subsequently be initialized by
	 * a call to {@link #initCause(Throwable)}.
	 * 
	 * @param message
	 *            the detail {@code message}. The detail {@code message} is
	 *            saved for later retrieval by the {@link #getMessage()} method.
	 * @see TransactionException#TransactionException(String)
	 */
	public IllegalStateException(String message) {
		super(message);
	}

}
