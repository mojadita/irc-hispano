/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: Fri Jun 3rd, 2016, 09:55
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

public class Event<
		EG extends EventGenerator<EG, T, M>, 
		T extends Enum<T>, 
		M extends Message<T>> 
{
	
	private long 		m_timestamp;
	private EG			m_source;
	private M			m_message;

	public Event(long timestamp, EG source, M message) {
		m_timestamp = timestamp;
		m_source = source;
		m_message = message;
	}
	
	public Event(EG source, M message) {
		this(System.currentTimeMillis(), source, message);
	}
	
	public long getTimestamp() {
		return m_timestamp;
	}
	
	public EG getSource() {
		return m_source;
	}

	public M getMessage() {
		return m_message;
	}
}