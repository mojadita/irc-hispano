/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 27 de abr. de 2016, 21:24:21
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * message type.  
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class IRCMessage extends Common {
	
	private static final long serialVersionUID = -8756960660302415524L;
	
	public static final String PROPERTY_ORIGIN = "origin";
	public static final String PROPERTY_CODE   = "code";
	public static final String PROPERTY_PARAMS = "params";
	
	private static final Pattern hasSpaces     = Pattern.compile(".*[\\t ].*");
	
	Origin            m_origin;
	IRCCode		  m_code;
	ArrayList<String> m_params;
	
	/**
	 * Complete constructor with {@link Origin} and parameter list as a
	 * {@code {@link Collection}<{@link String}>}.
	 * 
	 * @param origin the {@link Origin} of the message.
	 * @param code the {@link IRCCode} for this message.
	 * @param params the {@link Collection} of {@link String} parameters that will
	 * be the message parameters.
	 */
	public IRCMessage(Origin origin, IRCCode code, Collection<String> params) {
		m_origin = origin;
		m_code   = code;
		m_params = new ArrayList<String>();
		for (String p: params) {
			m_params.add(p);
		}
	}
	
	/**
	 * Complete constructor with {@link Origin} and variable parameter list.
	 * 
	 * @param origin the {@link Origin} of the message.
	 * @param code the {@link IRCCode} of the message.
	 * @param params the {@link String} list of parameters.
	 */
	public IRCMessage(Origin origin, IRCCode code, String...params) {
		m_origin = origin;
		m_code = code;
		m_params = new ArrayList<String>();
		for (String p: params) {
			m_params.add(p);
		}
	}
	
	/**
	 * Constructor without {@link Origin} and with parameter list as a
	 * {@code {@link Collection}<{@link String}>}.
	 * 
	 * @param params the {@link Collection} of {@link String} parameters.
	 */
	public IRCMessage(IRCCode code, Collection<String> params) {
		m_code = code;
		m_params = new ArrayList<String>();
		for (String p: params) {
			m_params.add(p);
		}
	}
	
	/**
	 * Constructor without {@link Origin} and parameter list as a
	 * {@code {@link String}[]}.
	 * 
	 * @param params the {@link String} array of parameters.
	 */
	public IRCMessage(IRCCode code, String...params) {
		m_code = code;
		m_params = new ArrayList<String>();
		for (String p: params) {
			m_params.add(p);
		}
	}
	
	/**
	 * Getter for the {@code Origin origin} property.
	 * @return the {@code Origin} value of the 
	 * property {@code origin}
	 */
	public Origin getOrigin() {
		return m_origin;
	}
	
	/**
	 * Setter for {@code Origin} property {@code origin}.
	 * @param origin the {@code Origin} value to set origin property to.
	 */
	public void setOrigin(Origin origin) {
		Origin oldOrigin = getOrigin();
		if (oldOrigin == origin) return;
		m_origin = origin;
		firePropertyChange(PROPERTY_ORIGIN, oldOrigin, origin);
	}
	
	/**
	 * Getter for the {@link IRCCode} {@code code} property.
	 * @return the {@link IRCCode} of this message.
	 */
	public IRCCode getCode() {
		return m_code;
	}
	
	/**
	 * Setter for the {@link IRCCode} {@code code} property.
	 * @param code the {@link IRCCode} to set this message to.
	 */
	public void setCode(IRCCode code) {
		IRCCode oldCode = getCode();
		if (oldCode == code) return;
		m_code = code;
		firePropertyChange(PROPERTY_CODE, oldCode, code);
	}
	
	/**
	 * Getter for the {@code ArrayList<String> params} property.
	 * @return the {@code ArrayList<String>} value of the 
	 * property {@code params}
	 */
	public ArrayList<String> getParams() {
		return m_params;
	}
	
	/**
	 * Setter for {@code ArrayList<String>} property {@code params}.
	 * @param params the {@code ArrayList<String>} value to set {@code params} property to.
	 */
	public void setParams(Collection<String> params) {
		ArrayList<String> oldParams = getParams();
		if (params == oldParams) return;
		m_params = new ArrayList<String>();
		m_params.addAll(params);
		firePropertyChange(PROPERTY_PARAMS, oldParams, params);
	}
	
	/**
	 * Convenience method to set programmatically the
	 * {@code {@link ArrayList}<{@link String}>} property {@code params}.
	 * @param params the parameters to be adjusted the property to.
	 */
	public void setParams(String...params) {
		ArrayList<String> oldParams = getParams();
		m_params = new ArrayList<String>();
		for (String s:params)
			m_params.add(s);
		firePropertyChange(PROPERTY_PARAMS, oldParams, m_params);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Origin o = getOrigin();
		if (o != null)
			sb.append(":" + o + " ");
		sb.append(getCode().getName());
		boolean notDoneBefore = true;
		for (String p: getParams()) {
			sb.append(" ");
			if (notDoneBefore && hasSpaces.matcher(p).matches()) {
				notDoneBefore = false;
				sb.append(":");
			}
			sb.append(p);
		}
		return sb.toString();
	}
	
	public byte[] getBytes() {
		return (toString()+"\r\n").getBytes();
	}
}
