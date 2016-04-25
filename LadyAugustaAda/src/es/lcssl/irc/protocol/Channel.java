/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 25 de abr. de 2016, 15:18:28
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

/**
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class Channel extends Common {

	public static enum TargetType {
		NORMAL_CHANNEL("#"),
		MODELESS_CHANNEL("+"),
		LOCAL_CHANNEL("&"),
		;
		
		private String m_pfx;
		
		TargetType(String pfx) {
			m_pfx = pfx;
		}
		
		public String getPfx() {
			return m_pfx;
		}
	}
	
	private static final long serialVersionUID = -6146976101528753836L;
	
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_ID = "id";

	private TargetType m_type;
	private String m_id;
	
	public Channel(TargetType type, String id) {
		m_type = type; m_id = id;
	}
	
	public Channel(String name) throws IllegalArgumentException {
		char first = name.charAt(0);
		switch(first) {
		case '#': m_type = TargetType.NORMAL_CHANNEL; break;
		case '+': m_type = TargetType.MODELESS_CHANNEL; break;
		case '&': m_type = TargetType.LOCAL_CHANNEL; break;
		default: throw new IllegalArgumentException(
				"Illegal char '" + first + "' as channel type");
		}
		m_id = name.substring(1);
	}
	
	/* (non-Javadoc)
	 * @see es.lcssl.irc.protocol.Common#protocolValue()
	 */
	@Override
	public String protocolValue() {
		return m_type.getPfx() + m_id;
	}

	/**
	 * Getter for the {@code TargetType type} property.
	 * @return the {@code TargetType} value of the 
	 * property {@code type}
	 */
	public TargetType getType() {
		return m_type;
	}

	/**
	 * Setter for {@Link TargetType} property {@code type}.
	 * @param type the {@link TargetType} value to set type property to.
	 */
	public void setType(TargetType type) {
		TargetType oldType = getType();
		if (oldType == type) return;
		m_type = type;
		firePropertyChange(PROPERTY_TYPE, oldType, type);
	}

	/**
	 * Getter for the {@code String id} property.
	 * @return the {@code String} value of the 
	 * property {@code id}
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * Setter for {@code String} property {@code id}.
	 * @param id the {@code String} value to set id property to.
	 */
	public void setId(String id) {
		String oldId = getId();
		if (oldId != null && oldId.equals(id)) return;
		if (id != null && id.equals(oldId)) return;
		if (id == oldId) return;
		m_id = id;
		firePropertyChange(PROPERTY_ID, oldId, id);
	}
}
