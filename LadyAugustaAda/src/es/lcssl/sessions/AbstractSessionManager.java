/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 2 de jul. de 2016 23:59:33.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.monitors
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.sessions;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class AbstractSessionManager<
		SF extends SessionFactory<SF, SM, K, S>, 
		SM extends SessionManager<SF, SM, K, S>, 
		K extends Comparable<? super K>, 
		S extends Session<SF, SM, K, S>> 
extends Thread
implements SessionManager<SF, SM, K, S> 
{
	private SF m_sessionFactory;
	private S m_session;
	private int m_exitCode;
	private BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_queue;
	
	public AbstractSessionManager(SF sessionFactory) {
		m_sessionFactory = sessionFactory;
		m_queue = new LinkedBlockingQueue<Event<Monitor, IRCCode, IRCMessage>>();
	}

	@Override
	public void run() {
		System.out.println("BEGIN session: " + getName());
		m_exitCode = m_session.run();
		System.out.println("END session: " + getName() + " with code " + m_exitCode);
	}

	@Override
	public Event<Monitor, IRCCode, IRCMessage> getEvent() 
			throws InterruptedException {
		return m_queue.take();
	}

	@Override
	public Event<Monitor, IRCCode, IRCMessage> getEvent(long timespec, TimeUnit unit) 
			throws InterruptedException {
		return m_queue.poll(timespec, unit);
	}

	@Override
	public S getSession() {
		return m_session;
	}

	@Override
	public SF getSessionFactory() {
		return m_sessionFactory;
	}

	@Override
	public void putEvent(Event<Monitor, IRCCode, IRCMessage> event) 
			throws InterruptedException {
		m_queue.put(event);
	}

	@Override
	public int getExitCode() {
		return m_exitCode;
	}

	/**
	 * Getter for the {@link Thread} {@code thread} attribute.
	 * @return the value of the {@code thread}.
	 */
	public Thread getThread() {
		return this;
	}

	/**
	 * @param session
	 * @see es.lcssl.sessions.SessionManager#setSession(es.lcssl.sessions.Session)
	 */
	@Override
	public void setSession(S session) {
		m_session = session;
	}
}
