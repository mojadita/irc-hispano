/**
 * 
 */
package es.lcssl.sessions;

import java.util.Collection;

/**
 * @author lcu
 *
 */
public interface SessionFactory<
		SF extends SessionFactory<SF,SM,K,S>, 
		SM extends SessionManager<SF,SM,K,S>,
		K extends Comparable<? super K>, 
		S extends Session<SF,SM,K,S>> 
{
	
	/**
	 * Looks for {@link Session} with {@code key} attribute.
	 * 
	 * @param key
	 *            the identifier of the requested {@link Session}.
	 * @param abstractSessionManager
	 *            is the {@link SessionManager} in charge for this session. It
	 *            is needed only in case a new {@link Session} has to be
	 *            created.
	 * @return the {@link Session} corresponding to {@code key}. If the session
	 *         indicated doesn't exist, it will be created, so this method
	 *         should never return {@code null}.
	 */
	S lookupSession(K key);
	
	/**
	 * Getter for the {@code sessions} attribute.
	 * @return a {@link Collection} of the active {@link Session} at the time
	 * of the call.
	 */
	Collection<S> getSessions();
	
	/**
	 * @return the {@code SM} {@code sessionManagers} attribute.
	 */
	Collection<SM> getSessionManagers();

}
