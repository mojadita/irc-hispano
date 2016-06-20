/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 29 de may. de 2016, 9:07:35
 * Project: LadyAugustaAda
 * Package: el.lcssl.irc.bot
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.bot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import es.lcssl.irc.monitors.COMMANDSession;
import es.lcssl.irc.monitors.COMMANDSessionFactory;
import es.lcssl.irc.monitors.MessageTracer;
import es.lcssl.irc.monitors.PRIVMSGMonitor;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP;

/**
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class TestIrcSAPBot {
	
	public static final String PROPERTY_IRC_SERVER = "irc.server.address";
	public static final String PROPERTY_IRC_PORT = "irc.server.port";
	public static final String PROPERTY_CHANNELS = "irc.client.channels";
	public static final String PROPERTY_ADMINISTRATORS = "irc.client.administrators";

	static Properties 	props;
	static IrcSAP 		sap;

	public static void main(String[] args) {
		try {

			props = new Properties();
			props.load(new BufferedInputStream(
					new FileInputStream(
							TestIrcSAPBot.class.getSimpleName().toLowerCase() 
							+ ".properties")));
			sap = new IrcSAP(
					props.getProperty(PROPERTY_IRC_SERVER), 
					Integer.decode(props.getProperty(PROPERTY_IRC_PORT, "6667")), 
					props);
			
			sap.getOutputMonitor().register(new MessageTracer(props, " <-- "));
			sap.getInputMonitor().register(new MessageTracer(props, " --> "));

			sap.start();

			Set<String> adminsSet = new TreeSet<String>();
			for (String s: props.getProperty(PROPERTY_ADMINISTRATORS, "").split(",")) 
				adminsSet.add(s.toLowerCase());
			sap.getInputMonitor().register(
					IRCCode.PRIVMSG, 
					new PRIVMSGMonitor<COMMANDSession>(
							sap.getNick(), 
							adminsSet, 
							new COMMANDSessionFactory(props)));

			String channels = props.getProperty(PROPERTY_CHANNELS);
			if (channels != null)
				sap.addMessage(new IRCMessage(IRCCode.JOIN, channels));
		} catch (FileNotFoundException e) {
			System.err.println("" + e);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
