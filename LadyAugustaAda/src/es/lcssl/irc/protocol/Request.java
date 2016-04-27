/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 27 de abr. de 2016, 21:40:12
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.util.Collection;

/**
 * Request Message.
 * 
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class Request extends BasicMessage {
	
	private static final long serialVersionUID = 2955429510211289247L;
	
	public static final String PROPERTY_CODE = "code";
	
	RequestCode m_code;
	
	public Request(Origin origin, RequestCode code, Collection<String> params) {
		super(origin, params);
		m_code = code;
	}
	
	public Request(Origin origin, RequestCode code, String... params) {
		super(origin, params);
		m_code = code;
	}

	public Request(RequestCode code, Collection<String> params) {
		super(params);
		m_code = code;
	}
	
	public Request(RequestCode code, String... params) {
		super(params);
		m_code = code;
	}

	/**
	 * Getter for the {@code RequestCode code} property.
	 * @return the {@code RequestCode} value of the 
	 * property {@code code}
	 */
	public RequestCode getCode() {
		return m_code;
	}

	/**
	 * Setter for {@code RequestCode} property {@code code}.
	 * @param code the {@code RequestCode} value to set code property to.
	 */
	public void setCode(RequestCode code) {
		RequestCode oldCode = getCode();
		if (oldCode == code) return;
		m_code = code;
		firePropertyChange(PROPERTY_CODE, oldCode, code);
	}

	/* (non-Javadoc)
	 * @see es.lcssl.irc.protocol.Common#protocolValue()
	 */
	@Override
	public String protocolValue() {
		StringBuilder sb = new StringBuilder();
		Origin o = getOrigin();
		if (o != null) {
			sb.append(o).append(" ");
		}
		sb.append(m_code.name());
		
		boolean inLast = false;
		
		for (String p: getParams()) {
			sb.append(' ');
			if (p.indexOf(' ') >= 0) {
				if (!inLast) {
					sb.append(':');
					inLast = true;
				}
			}
			sb.append(p);
		}
		return sb.toString();
	}
}

