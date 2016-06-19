/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 18 de jun. de 2016 11:34:13.
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
public enum TransactionState {
	IDLE(false),
	STARTED(false),
	ENDED(true),
	ERROR(true),
	;
	
	private boolean m_final;
	
	TransactionState(boolean fin) {
		m_final = fin;
	}
	
	/**
	 * @return {@code true} if the state is a final state (meaning transaction has ended)
	 */
	public boolean isFinal() {
		return m_final;
	}
}
