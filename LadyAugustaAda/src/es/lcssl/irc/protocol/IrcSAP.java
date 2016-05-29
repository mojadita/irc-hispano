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
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
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
	public static final String PROPERTY_IDENT = "irc.user.ident";
	public static final String PROPERTY_NAME = "irc.user.name";
	public static final String PROPERTY_MODES = "irc.user.modes";
	public static final String PROPERTY_PASSWORD = "irc.user.password";
	public static final String PROPERTY_DONTDOPONG = "irc.user.dontdopong";
	
	private Socket 						m_socket;
	private InputStream 				m_in;
	private OutputStream 				m_out;
	private Properties 					m_properties;
	private BlockingQueue<IRCMessage> 	m_outq;
	private Thread 						m_outmonitor;
	private Thread 						m_inmonitor;
	private Map<IRCCode, MyObservable> 	m_inMap;
	private MyObservable 				m_inObservable;
	private Map<IRCCode, MyObservable> 	m_outMap;
	private MyObservable 				m_outObservable;
	private volatile String				m_nick;
	
	public class Event {
		
		private long 		m_timestamp;
		private IRCMessage	m_message;

		public Event(long timestamp, IRCMessage message) {
			m_timestamp = timestamp;
			m_message = message;
		}
		
		public long getTimestamp() {
			return m_timestamp;
		}
		
		public IRCMessage getMessage() {
			return m_message;
		}
	}
	
	private class MyObservable extends Observable {
		public synchronized void setChanged() {
			super.setChanged();
		}
		public IrcSAP getIrcSAP() {
			return IrcSAP.this;
		}
	}
	
	protected class InputMonitor implements Runnable {
		@Override
		public void run() {
			System.out.println(getClass() + " Running...");
			IRCParser p = new IRCParser(m_in);
			IRCMessage m;
			try {
				while ((m = p.scan()) != null) {
					long timestamp = System.currentTimeMillis();
					m_inObservable.setChanged();
					m_inObservable.notifyObservers(new Event(timestamp, m));
					MyObservable o = m_inMap.get(m.getCode());
					if (o != null) {
						o.setChanged();
						o.notifyObservers(new Event(timestamp, m));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			m_outmonitor.interrupt();
			System.out.println(getClass() + " Finishing...");
		}
	}
	
	protected class OutputMonitor implements Runnable {
		@Override
		public void run() {
			System.out.println(getClass() + " Running...");
			try {
				while (true) {
					IRCMessage m = m_outq.take();
					long timestamp = System.currentTimeMillis();
					MyObservable o = m_outMap.get(m.getCode());
					if (o != null) {
						o.setChanged();
						o.notifyObservers(new Event(timestamp,m));
					}
					m_outObservable.setChanged();
					m_outObservable.notifyObservers(new Event(timestamp,m));
					m_out.write(m.getBytes());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
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
		m_outmonitor = new Thread(new OutputMonitor());
		m_inmonitor = new Thread(new InputMonitor());
		m_inMap = new EnumMap<IRCCode, MyObservable>(IRCCode.class);
		m_outMap = new EnumMap<IRCCode, MyObservable>(IRCCode.class);
		m_inObservable = new MyObservable();
		m_outObservable = new MyObservable();
	}
	
	public void start() {
		addInObserver(IRCCode.PING, new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				MyObservable mo = (MyObservable) o;
				Event e = (Event) arg;
				IRCMessage m = e.getMessage();
				m.setCode(IRCCode.PONG);
				mo.getIrcSAP().addMessage(m);
			}
		});
		addInObserver(IRCCode.RPL_WELCOME, new Observer(){
			@Override
			public void update(Observable o, Object arg) {
				m_nick = ((Event) arg).getMessage().getParams().get(0);
				System.out.println("NICK = " + m_nick);
				o.deleteObserver(this);
			}
		});
		String modes = m_properties.getProperty(PROPERTY_MODES);
		if (modes != null) {
			addInObserver(IRCCode.MODE, new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					try {
						Thread.sleep(200);
						addMessage(new IRCMessage(IRCCode.MODE, m_nick, modes));
						o.deleteObserver(this);
					} catch (InterruptedException e) {
					}
				}
			});
		}
		m_inmonitor.start();
		m_outmonitor.start();
		String passwd = m_properties.getProperty(PROPERTY_PASSWORD);
		if (passwd != null)
			addMessage(new IRCMessage(IRCCode.PASS, passwd));
		String nick = m_properties.getProperty(PROPERTY_NICK, "LadyAugustaAda");
		addMessage(new IRCMessage(IRCCode.NICK, nick));
		addMessage(new IRCMessage(IRCCode.USER, 
				m_properties.getProperty(PROPERTY_IDENT, "ada"),
				"0", "*", 
				m_properties.getProperty(PROPERTY_NAME, 
						"Countess of Lovelace, daughter of Lord Byron")));
	}
	
	public void addInObserver(IRCCode cod, Observer obs) {
		MyObservable o = m_inMap.get(cod);
		if (o == null)
			m_inMap.put(cod, o = new MyObservable());
		o.addObserver(obs);
	}
	
	public void deleteInObserver(IRCCode cod, Observer obs) {
		MyObservable o = m_inMap.get(cod);
		if (o != null)
			o.deleteObserver(obs);
	}
	
	public void addInObserver(Observer obs) {
		m_inObservable.addObserver(obs);
	}
	
	public void deleteInObserver(Observer obs) {
		m_inObservable.deleteObserver(obs);
	}
	
	public synchronized void addOutObserver(IRCCode cod, Observer obs) {
		MyObservable o = m_outMap.get(cod);
		if (o == null)
			m_outMap.put(cod, o = new MyObservable());
		o.addObserver(obs);
	}
	
	public synchronized void deleteOutObserver(IRCCode cod, Observer obs) {
		MyObservable o = m_outMap.get(cod);
		if (o != null)
			o.deleteObserver(obs);
	}
	
	public synchronized void addOutObserver(Observer obs) {
		m_outObservable.addObserver(obs);
	}
	
	public void deleteOutObserver(Observer obs) {
		m_outObservable.deleteObserver(obs);
	}
	
	public void addMessage(IRCMessage m) {
		m_outq.add(m);
	}
} /* IrcSAP */
