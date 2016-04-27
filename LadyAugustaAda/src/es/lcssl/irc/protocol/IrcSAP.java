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
		INITIAL, ORIGIN, PRE_COMMAND, COMMAND, INTER_PARM, MIDDLE, FINAL, CR,
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
					StringBuilder origStr = null;
					StringBuilder command = null;

					switch (st) {
					case INITIAL:
						switch (ch) {
						case ':': st = Status.ORIGIN; 
							origStr = new StringBuilder(); 
							continue;
						case ' ': case '\t': continue;
						case '\r': st = Status.CR; continue;
						case '\n': continue;
						default:
							st = Status.COMMAND;
							command = new StringBuilder(ch);
							continue;
						} 
					case ORIGIN:
						switch (ch) {
						case '\t': case ' ': 
							st = Status.COMMAND;
							command = new StringBuilder();
							continue;
						case '\r': st = Status.CR; continue;
						case '\n': st = Status.INITIAL; continue;
						default: origStr.append(ch); continue;
						}
					case PRE_COMMAND:
						switch (ch) {
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						default:
						} break;
					case COMMAND:
						switch (ch) {
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						default:
						} break;
					case INTER_PARM:
						switch (ch) {
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						default:
						} break;
					case MIDDLE:
						switch (ch) {
						case ':':
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						default:
						} break;
					case FINAL:
						switch (ch) {
						case ':':
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						default:
						} break;
					case CR:
						switch (ch) {
						case ':':
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						default:
						} break;
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
