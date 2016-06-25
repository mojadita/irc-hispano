/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 25 de jun. de 2016 11:54:49.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.access
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.access;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class AdminsTableFactory<T extends Comparable<? super T>> 
extends TreeMap<String, AdminsTableFactory<T>.AdminsTable> 
{
	
	private static final long serialVersionUID = 8016204255952757850L;
	
	public class AdminsTable extends TreeSet<T> {
		
		private static final long serialVersionUID = -3887485347754476284L;
		
		String m_name;
		AdminsTable m_parent;
		
		public AdminsTable(AdminsTable parent, String name) {
			m_name = name;
			m_parent = parent;
			put(name , this);
		}
		
		public AdminsTable(String name) {
			this(null, name);
		}
		
		public boolean checkAccessToResources(T target) {
			boolean result;
			result = contains(target);
			if (!result && m_parent != null) 
				result = m_parent.checkAccessToResources(target);
			return result; 
		}
		
		public boolean checkAccessToThis(T target) {
			return m_parent == null 
					? checkAccessToResources(target) 
					: m_parent.checkAccessToResources(target);
		}
		
		public String getName() {
			return m_name;
		}
		
		public AdminsTable getParent() {
			return m_parent;
		}
		
		public void setParent(AdminsTable newParent) {
			m_parent = newParent;
		}
	}

	public AdminsTable lookup(AdminsTable parent, String name) {
		AdminsTable result = get(name);
		if (result == null) {
			result = new AdminsTable(parent, name);
		}
		return result;
	}
	
	public AdminsTable lookup(String name) {
		return lookup(null, name);
	}
}
