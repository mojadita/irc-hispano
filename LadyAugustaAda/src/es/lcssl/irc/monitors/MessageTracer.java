package es.lcssl.irc.monitors;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

public class MessageTracer 
extends Thread
implements EventListener<Monitor,IRCCode,IRCMessage>
{
	
	public static final String PROPERTY_COLORFORMAT = "irc.client.colorformat";

	BlockingQueue<Event<Monitor, IRCCode, IRCMessage>> m_queue;
	Properties m_props;
	IrcSAP m_sap;
	long lastTimestamp = 0;

	public MessageTracer(IrcSAP sap, Properties props) {
		m_sap = sap;
		m_props = props;
		m_queue = new LinkedBlockingQueue<Event<Monitor,IRCCode,IRCMessage>>();
		m_sap.getInputMonitor().register(this);
		m_sap.getOutputMonitor().register(this);
		setDaemon(true);
		start();
	}
	
	@Override
	public void process(Event<Monitor, IRCCode, IRCMessage> event) {
		m_queue.add(event);
	}
	
	@Override
	public void run() {
		try {
			for (;;) {
				Event<Monitor, IRCCode, IRCMessage> ev = m_queue.take();
				long timestamp = ev.getTimestamp();
				long lap = lastTimestamp == 0 ? 0 : timestamp - lastTimestamp;
				System.out.println((ev.getSource() == m_sap.getInputMonitor() ? "--> " : "<-- ")
						+ new Date(ev.getTimestamp()) 
						+ String.format(" (%5d.%03d)", lap / 1000, lap % 1000)
						+ ": " + (ev.getMessage().getCode().isReply() 
								? ev.getMessage().getCode() + " " 
								: "") 
						+ ev.getMessage());
				lastTimestamp = timestamp;
			}
		} catch (InterruptedException e) {
			System.out.println(this + " interrupted, ending.");
		}
	}
}