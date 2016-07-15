/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 28 de jun. de 2016 17:13:57.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.monitors.nick
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.monitors.nick;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class NickMonitor 
extends Thread 
implements EventListener<Monitor, IRCCode, IRCMessage> 
{
	
	IrcSAP m_sap;
	BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_in;
	volatile boolean m_run;
	Map<String, Origin> m_nickMap;
	
	public NickMonitor(IrcSAP sap) {
		m_sap = sap;
		m_in = new LinkedBlockingQueue<>();
		m_run = true;
		m_nickMap = new TreeMap<>();
	}
	
	@Override
	public void start() {
		Monitor mon = m_sap.getInputMonitor();
		mon.register(IRCCode.NICK, this);
		mon.register(IRCCode.AWAY, this);
		mon.register(IRCCode.JOIN, this);
		mon.register(IRCCode.PART, this);
		mon.register(IRCCode.QUIT, this);
		setDaemon(true);
		super.start();
	}
	
	public void unregister() {
		Monitor mon = m_sap.getInputMonitor();
		mon.unregister(IRCCode.NICK, this);
		mon.unregister(IRCCode.AWAY, this);
		mon.unregister(IRCCode.JOIN, this);
		mon.unregister(IRCCode.PART, this);
		mon.unregister(IRCCode.QUIT, this);
	}
	
	/**
	 * @param event
	 * @see es.lcssl.irc.protocol.EventListener#process(es.lcssl.irc.protocol.Event)
	 */
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		m_in.add(event);
	}
	
	@Override
	public void run() {
		try {
			Event<Monitor, IRCCode, IRCMessage> theEvent;
			while (m_run) {
				theEvent = m_in.take();
				long timeStamp = theEvent.getTimestamp();
				Monitor theMonitor = theEvent.getSource();
				IRCMessage theMessage = theEvent.getMessage();
				IRCCode code = theMessage.getCode();
				switch(code) {
				case NICK:
				case AWAY:
				case JOIN:
				case PART:
				case QUIT:
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			unregister();
		}
	}

}
