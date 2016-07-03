/**
 * 
 */
package es.lcssl.sessions;

import es.lcssl.irc.protocol.Event;

/**
 * A {@link Session} is a {@link Thread} with some specific inteface to be able
 * to get {@link Event}s from the {@link SessionManager} and process them.
 * <p>
 * {@link Session}s must implement a {@link #run(SessionManager)} method which
 * is run in an independent {@link Thread}. The {@link SessionManager} acts as a
 * demultiplexor of incoming events and dispatches them to the proper
 * {@link Session} instance by means of its input queue. The instances must call
 * {@link SessionManager#getEvent()} or
 * {@link SessionManager#getEvent(long, java.util.concurrent.TimeUnit)} methods,
 * to get access to the input {@link Event}s.
 * 
 * @author lcu
 *
 */
public interface Session<
		SF extends SessionFactory<SF, SM, K, S>, 
		SM extends SessionManager<SF, SM, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<SF, SM, K, S>> 
{

	/**
	 * The method that executes while this {@link Session} is active. The
	 * {@link Session} will be active for the whole duration of this method
	 * execution and will die once the method finishes execution.
	 * 
	 * @param abstractSessionManager
	 *            The {@link SessionManager} in charge of this {@link Session}.
	 *            It uses the {@link SessionManager} to get access to the input
	 *            events.
	 * @return termination code for the {@link SessionManager}. What the
	 *         {@link SessionManager} does with this code is implementation
	 *         dependant.
	 */
	int run();

	/**
	 * @return the {@code K key} attribute for this {@link Session}.
	 */
	K getKey();

	/**
	 * @return the {@link SessionManager} controlling this {@link Session}.
	 */
	SM getSessionManager();
	
	/**
	 * @param sessionManager the {@link SessionManager} to set the 
	 * {@code sessionManager} attribute.
	 */
	void setSessionManager(SM sessionManager);

	/**
	 * @return the reference to the {@link SessionFactory} that created this {@link Session}.
	 */
	SF getSessionFactory();
}
