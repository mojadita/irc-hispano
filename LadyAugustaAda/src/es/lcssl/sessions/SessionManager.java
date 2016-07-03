/**
 * 
 */
package es.lcssl.sessions;

import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * @author lcu
 *
 */
public interface SessionManager<
		SF extends SessionFactory<SF,SM,K,S>, 
		SM extends SessionManager<SF,SM,K,S>,
		K extends Comparable<? super K>, 
		S extends Session<SF,SM,K,S>> 
extends Runnable
{
	/**
	 * This method allows managed {@link Session}s to get the next event.
	 * @return the next {@link Event} or {@code null} in case there's no more
	 * data to continue the transaction. The method blocks until an event is
	 * present in the event queue.
	 * @throws InterruptedException in case the {@link Thread} running this
	 * {@link Session} is interrupted while waiting for the next event.
	 */
	Event<Monitor, IRCCode, IRCMessage> getEvent() 
			throws InterruptedException;

	/**
	 * Timeout version of the {@link #getEvent()} method. Allows to specify
	 * the units for the waiting period.
	 * @param timespec Time to wait for a new {@link Event}.
	 * @param unit Units in which {@code timespec} is specified.
	 * @return the next {@link Event} or {@code null} in case no {@link Event}
	 * arribes in the time specified.
	 * @throws InterruptedException in case the waiting {@link Thread} receives
	 * an interrupt.
	 */
	Event<Monitor, IRCCode, IRCMessage> getEvent(long timespec, TimeUnit unit) 
			throws InterruptedException;
	
	void putEvent(Event<Monitor, IRCCode, IRCMessage> event) 
			throws InterruptedException;
	
	/**
	 * @return the corresponding {@link Session}.
	 */
	S getSession();
	
	void setSession(S session);
	
	/**
	 * @return the corresponding {@link SessionFactory}.
	 */
	SF getSessionFactory();
	
	/**
	 * @return the exit code from the executed thread.
	 */
	int getExitCode();
	
	/**
	 * @return if the session is alive. Becomes {@code false} when the {@link Session}
	 * is not alive anymore.
	 */
	boolean isAlive();
	
}
