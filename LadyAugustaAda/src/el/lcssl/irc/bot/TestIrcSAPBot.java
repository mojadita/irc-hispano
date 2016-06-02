/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 29 de may. de 2016, 9:07:35
 * Project: LadyAugustaAda
 * Package: el.lcssl.irc.bot
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package el.lcssl.irc.bot;

import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;
import es.lcssl.irc.protocol.IrcSAP.Event;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

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
	
	public static class Tracer implements Observer {
		static long lastMessage;
		String m_label;
		public Tracer(String label) {
			m_label = label;
		}
		@Override
		public void update(Observable o, Object arg) {
			Event e = (Event) arg;
			long timestamp = e.getTimestamp();
			long lap = timestamp - lastMessage;
			System.out.println(
					String.format("%5d.%03d:", lap/1000, lap%1000)
					+ m_label + e.getMessage());
			lastMessage = timestamp;
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			props = new Properties();
			try {
			props.load(new BufferedInputStream(
					new FileInputStream(
							TestIrcSAPBot.class.getSimpleName().toLowerCase() 
							+ ".properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			sap = new IrcSAP(
					props.getProperty(PROPERTY_IRC_SERVER), 
					Integer.decode(props.getProperty(PROPERTY_IRC_PORT, "6667")), 
					props);
			sap.addInObserver(new Tracer(" <-- "));
			sap.addOutObserver(new Tracer(" --> "));
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
