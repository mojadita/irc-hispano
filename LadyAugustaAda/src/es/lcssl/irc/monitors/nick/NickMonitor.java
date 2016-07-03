/* $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com
 * Date: 28 de jun. de 2016 17:13:57.
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.monitors.nick
 * Disclaimer: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.monitors.nick;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * 
 *
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 */
public class NickMonitor extends Thread implements EventListener<Monitor, IRCCode, IRCMessage> {
	
	IrcSAP m_sap;
	Queue<Event<Monitor, IRCCode, IRCMessage>> m_in;
	
	public NickMonitor(IrcSAP sap) {
		m_sap = sap;
		m_in = new BlockingQueue<Event<Monitor,IRCCode,IRCMessage>>() {
		};
	}
	
	/**
	 * @param event
	 * @see es.lcssl.irc.protocol.EventListener#process(es.lcssl.irc.protocol.Event)
	 */
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		
	}
	
	@Override
	public void run() {
		
	}

}
