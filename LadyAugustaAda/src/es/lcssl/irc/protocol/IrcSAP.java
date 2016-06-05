/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 28 de abr. de 2016
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * IRC Service Access Point.
 * 
 * This class implements the interface to the network for a client IRC connection.
 * The IrcSAP must be started with {@link Thread#start()} method to begin the wheel 
 * rotating.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 */
public class IrcSAP {
	
	public static final String PROPERTY_NICK = "irc.user.nick";
	public static final String DEFAULT_NICK = "AdaAugustaLovelace";
	
	public static final String PROPERTY_IDENT = "irc.user.ident";
	public static final String DEFAULT_IDENT = "ada";
	
	public static final String PROPERTY_NAME = "irc.user.name";
	public static final String DEFAULT_NAME = "Daughter of Lord Byron and first programmer in History";
	
	public static final String PROPERTY_MODES = "irc.user.modes";

	public static final String PROPERTY_PASSWORD = "irc.user.password";

	public static final String PROPERTY_QUITMSG = "irc.user.quitmessage";
	public static final String DEFAULT_QUITMSG = "Good Bye";

	public static final String PROPERTY_NICKLIST = "irc.user.nicklist";
	public static final String DEFAULT_NICKLIST = "AdaAugustaByron,AdaLovelace,AdaByron,Ada,Ada_,Ada__,Ada___";
	
	private Socket 						m_socket;
	private InputStream 				m_in;
	private OutputStream 				m_out;
	private Properties 					m_properties;
	private BlockingQueue<IRCMessage> 	m_outq;
	private OutputMonitor				m_outmonitor;
	private InputMonitor 				m_inmonitor;
	private String						m_nick;
	private String[]					m_nickList;
	private int							m_nextToTry;
	private volatile State				m_state;
	
	public static enum State {
		UNREGISTERED,
		REGISTERED,
		EOF,
		ERROR,
	}
		
	public class Monitor
	extends EventGenerator<Monitor, IRCCode, IRCMessage> {
		public Monitor() {
			super(IRCCode.class);
		}
		public Properties getProperties() {
			return m_properties;
		}
		public String getNick() {
			return m_nick;
		}
		public IrcSAP getIrcSAP() {
			return IrcSAP.this;
		}
	}
	
	public class InputMonitor 
	extends Monitor 
	{
		@Override
		public void run() {
			System.out.println(getClass() + " Running...");
			IRCParser p = new IRCParser(m_in);
			IRCMessage m;
			try {
				while ((m = p.scan()) != null) {
					long timestamp = System.currentTimeMillis();
					fireEvent(timestamp, m);
				}
				synchronized(IrcSAP.this) {
					if (m_state == IrcSAP.State.UNREGISTERED)
						m_state = IrcSAP.State.ERROR;
					IrcSAP.this.notifyAll();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			m_outmonitor.interrupt();
			System.out.println(getClass() + " Finishing...");
		}
	}
	
	public class OutputMonitor extends Monitor {
		@Override
		public void run() {
			System.out.println(getClass() + " Running...");
			try {
				while (true) {
					IRCMessage m = m_outq.take();
					long timestamp = System.currentTimeMillis();
					fireEvent(timestamp, m);
					m_out.write(m.getBytes());
				}
			} catch (InterruptedException e) {
				// e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(getClass() + " Finishing...");
		}
	}
	
	/**
	 * Default constructor.
	 * @param host  IRC host to connect to.
	 * @param port TCP port the server is listening on.
	 * @param properties default configuration {@link Properties}.
	 * @throws UnknownHostException in case server host doesn't exist.
	 * @throws IOException on connection failure.
	 * @see Socket
	 * @see InputStreamReader
	 * @see BufferedInputStream
	 */
	public IrcSAP(String host, int port, Properties properties)
			throws UnknownHostException, IOException 
	{
		m_socket = new Socket(host, port);
		m_in = new BufferedInputStream(m_socket.getInputStream());
		m_out = m_socket.getOutputStream();
		m_properties = properties;
		m_outq = new LinkedBlockingQueue<IRCMessage>();
		m_outmonitor = new OutputMonitor();
		m_inmonitor = new InputMonitor();
		m_nextToTry = 0;
		m_state = State.UNREGISTERED;
	}
	
	public void start() throws InterruptedException {
		
		if (m_state != State.UNREGISTERED) return;

		// PING monitor.
		m_inmonitor.register(IRCCode.PING, new EventListener<Monitor, IRCCode, IRCMessage>(){
			@Override
			public void process(Monitor source,
					Event<Monitor, IRCCode, IRCMessage> event) {
				IRCMessage m = event.getMessage();
				addMessage(new IRCMessage(IRCCode.PONG, m.getParams()));
			}
		});
		
		// TO CHECK WHEN WE ARE REGISTERED OK
		m_inmonitor.register(IRCCode.RPL_WELCOME, 
				new EventListener<Monitor, IRCCode, IRCMessage>() {
			@Override
			public void process(Monitor source,
					Event<Monitor, IRCCode, IRCMessage> event) {
				IRCMessage m = event.getMessage();
				m_nick = m.getParams().get(0);
				synchronized(IrcSAP.this) {
					System.out.println("NICK == " + m_nick);
					m_state = State.REGISTERED;
					IrcSAP.this.notifyAll();
				}
				source.unregister(IRCCode.RPL_WELCOME, this); // don't need anymore.
			}
		});
		
		// TO CHECK FOR GLINES.
		m_inmonitor.register(IRCCode.ERR_YOUREBANNEDCREEP, 
				new EventListener<Monitor, IRCCode, IRCMessage>() {
			@Override
			public void process(Monitor source,
					Event<Monitor, IRCCode, IRCMessage> event) {
				IRCMessage m = event.getMessage();
				m_nick = m.getParams().get(0);
				synchronized(IrcSAP.this) {
					m_state = State.ERROR;
					System.err.println("GLINE detected, exiting...");
					IrcSAP.this.notifyAll();
				}
				addMessage(new IRCMessage(IRCCode.QUIT, m_properties.getProperty(
						PROPERTY_QUITMSG, DEFAULT_QUITMSG)));
				source.unregister(IRCCode.ERR_YOUREBANNEDCREEP, this);
			}
		});
		
		
		EventListener<Monitor, IRCCode, IRCMessage> newNickObserver =
				new EventListener<Monitor, IRCCode, IRCMessage> () {
			@Override
			public void process(Monitor source,
					Event<Monitor, IRCCode, IRCMessage> event) {
				IRCMessage m = event.getMessage();
				System.err.println(m.getCode().name() + ": " + m.getParams().get(1));
				if (m_nickList == null) {
					String nicklist = m_properties.getProperty(
							PROPERTY_NICKLIST, DEFAULT_NICKLIST);
					m_nickList = nicklist.split(",");
					System.out.println("NICKLIST = " + nicklist);
					m_nextToTry = 0;
				}
				if (m_nickList == null || m_nextToTry >= m_nickList.length) {
					addMessage(new IRCMessage(IRCCode.QUIT, m_properties.getProperty(
							PROPERTY_QUITMSG, DEFAULT_QUITMSG)));
					synchronized(IrcSAP.this) {
						m_state = State.ERROR;
						System.err.println("NICK LIST exhausted");
						IrcSAP.this.notifyAll();
					}
				} else {
					addMessage(new IRCMessage(IRCCode.NICK, m_nickList[m_nextToTry]));
					m_nextToTry++;
				}
			}
		};
		m_inmonitor.register(IRCCode.ERR_ERRONEUSNICKNAME, newNickObserver);
		m_inmonitor.register(IRCCode.ERR_NICKCOLLISION, newNickObserver);
		m_inmonitor.register(IRCCode.ERR_NICKNAMEINUSE, newNickObserver);
		
		m_inmonitor.start();
		m_outmonitor.start();
		
		// PASS message.
		String passwd = m_properties.getProperty(PROPERTY_PASSWORD);
		if (passwd != null)
			addMessage(new IRCMessage(IRCCode.PASS, passwd));
		
		// NICK message (should this ---and the next--- be changed to SERVICE ?)
		String nick = m_properties.getProperty(PROPERTY_NICK, DEFAULT_NICK);
		addMessage(new IRCMessage(IRCCode.NICK, nick));
		
		// USER message
		addMessage(new IRCMessage(IRCCode.USER, 
				m_properties.getProperty(PROPERTY_IDENT, "ada"),
				"0", "*", 
				m_properties.getProperty(PROPERTY_NAME, 
						"Countess of Lovelace, daughter of Lord Byron")));
		
		synchronized(this) {
			while (m_state == State.UNREGISTERED) wait();
		}
		m_inmonitor.unregister(IRCCode.ERR_ERRONEUSNICKNAME, newNickObserver);
		m_inmonitor.unregister(IRCCode.ERR_NICKCOLLISION, newNickObserver);
		m_inmonitor.unregister(IRCCode.ERR_NICKNAMEINUSE, newNickObserver);
	}
	
	public InputMonitor getInputMonitor() {
		return m_inmonitor;
	}
	
	public OutputMonitor getOutputMonitor() {
		return m_outmonitor;
	}
	
	public String getNick() {
		return m_nick;
	}
	
	public void addMessage(IRCMessage m) {
		m_outq.add(m);
	}
} /* IrcSAP */
