/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 29 de may. de 2016, 9:07:35
 * Project: LadyAugustaAda
 * Package: el.lcssl.irc.bot
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package el.lcssl.irc.bot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class TestIrcSAPBot {
	
	public static final String PROPERTY_IRC_SERVER = "irc.server.address";
	public static final String PROPERTY_IRC_PORT = "irc.server.port";
	public static final String PROPERTY_CHANNELS = "irc.client.channels";

	static Properties 	props;
	static IrcSAP 		sap;
	
	private static String color(int num, String s) {
		return String.format("\033[%dm%s\033[0m", num, s);
		//return s;
	}
	
	public static class Tracer implements EventListener<Monitor,IRCCode,IRCMessage> {
		
		static long lastMessage;
		String m_label;

		public Tracer(String label) {
			m_label = label;
		}
		
		@Override
		public void process(Monitor source,
				Event<Monitor, IRCCode, IRCMessage> event) {
			IRCMessage m = event.getMessage();
			long timestamp = event.getTimestamp();
			if (lastMessage == 0) lastMessage = timestamp;
			long lap = timestamp - lastMessage;
			System.out.println(
					String.format(color(36, "%5d.%03d"), lap/1000, lap%1000)
					+ m_label
					+ (m.getCode().isReply() 
							? color(m.getCode().isError() ? 31 : 32, m.getCode().name()) + ": " 
							: "")
					+ m);
			lastMessage = timestamp;
		}
	}
	
	
	public static void main(String[] args) {
		try {
			props = new Properties();
			try {
				props.load(new BufferedInputStream(
						new FileInputStream(
								TestIrcSAPBot.class.getSimpleName().toLowerCase() 
								+ ".properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
			sap = new IrcSAP(
					props.getProperty(PROPERTY_IRC_SERVER), 
					Integer.decode(props.getProperty(PROPERTY_IRC_PORT, "6667")), 
					props);
			sap.getInputMonitor().register(new Tracer(color(33, " --> ")));
			sap.getOutputMonitor().register(new Tracer(color(32, " <-- ")));
				
			sap.start();

			String channels = props.getProperty(PROPERTY_CHANNELS);
			if (channels != null)
				sap.addMessage(new IRCMessage(IRCCode.JOIN, channels));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
