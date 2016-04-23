/**
 * 
 */
package es.lcssl.irc.protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author luis
 *
 */
public class IrcSAP {
	
	private enum Status {
		INITIAL, NICK, IDENT, HOST, COMMAND, MIDDLE, FINAL, CR,
	}
	
	Socket 				m_socket;
	InputStreamReader 	m_istream;
	Monitor				m_monitor;
	
	private class Monitor extends Thread {

		@Override
		public void run() {
			int c;
			Status st = Status.INITIAL;
			try {
				while ((c = m_istream.read()) != -1) {
					final char ch = (char) c;
					switch (st) {
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public IrcSAP(String host, int port)
			throws UnknownHostException, IOException 
	{
		m_socket = new Socket(host, port);
		m_istream = new InputStreamReader(
				new BufferedInputStream(m_socket.getInputStream()));
		m_monitor = new Monitor();
		m_monitor.start();
	}
}
