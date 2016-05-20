/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 25 de abr. de 2016, 15:10:15
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class Common implements Serializable {
	
	/**
	 * The {@code long} {@code serialVersionUID}.
	 */
	private static final long serialVersionUID = -2254538933719665291L;
	
	private PropertyChangeSupport m_pcs;
	
	public Common() {
		m_pcs = new PropertyChangeSupport(this);
	}
	
	/**
	 * Add a PropertyChangeListener to the listener list. The listener is
	 * registered for all properties. The same listener object may be added more
	 * than once, and will be called as many times as it is added. If listener
	 * is null, no exception is thrown and no action is taken.
	 * 
	 * @param listener
	 *            The {@link PropertyChangeListener} to be added
	 * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		m_pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Add a PropertyChangeListener for a specific property. The listener will
	 * be invoked only when a call on firePropertyChange names that specific
	 * property. The same listener object may be added more than once. For each
	 * property, the listener will be invoked the number of times it was added
	 * for that property. If propertyName or listener is null, no exception is
	 * thrown and no action is taken.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on.
	 * @param listener
	 *            The {@link PropertyChangeListener} to be added
	 * @see PropertyChangeSupport#addPropertyChangeListener(String,
	 *      PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		m_pcs.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * Remove a PropertyChangeListener from the listener list. This removes a
	 * PropertyChangeListener that was registered for all properties. If
	 * listener was added more than once to the same event source, it will be
	 * notified one less time after being removed. If listener is null, or was
	 * never added, no exception is thrown and no action is taken.
	 * 
	 * @param listener
	 *            The {@link PropertyChangeListener} to be removed.
	 * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		m_pcs.removePropertyChangeListener(listener);
	}

	/**
	 * Remove a PropertyChangeListener for a specific property. If listener was
	 * added more than once to the same event source for the specified property,
	 * it will be notified one less time after being removed. If propertyName is
	 * null, no exception is thrown and no action is taken. If listener is null,
	 * or was never added for the specified property, no exception is thrown and
	 * no action is taken.
	 * 
	 * @param propertyName
	 *            The name of the property that was listened on.
	 * @param listener
	 *            The PropertyChangeListener to be removed.
	 * @see PropertyChangeSupport#removePropertyChangeListener(String,
	 *      PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		m_pcs.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Reports a bound property update to listeners that have been registered to
	 * track updates of all properties or a property with the specified name.
	 * 
	 * <p>
	 * No event is fired if old and new values are equal and non-null.
	 * 
	 * @param propertyName
	 *            the programmatic name of the property that was changed
	 * @param oldValue
	 *            the old value of the property.
	 * @param newValue
	 *            the new value of the property.
	 * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
	 */
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
}