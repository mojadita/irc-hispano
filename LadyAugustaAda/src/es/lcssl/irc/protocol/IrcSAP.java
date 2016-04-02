/**
 * 
 */
package es.lcssl.irc.protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import javax.security.auth.callback.TextInputCallback;

/**
 * @author luis
 *
 */
public class IrcSAP {
	
	Socket 				m_socket;
	InputStreamReader 	m_istream;
	
	private class Monitor extends Thread {

		@Override
		public void run() {
			StringBuilder sb = new StringBuilder();
			int c;
			try {
				while ((c = m_istream.read()) != -1) {
					final char ch = (char) c;
					switch (ch) {
					
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public IrcSAP(String host, int port, Charset cs)
			throws UnknownHostException, IOException 
	{
		m_socket = new Socket(host, port);
		m_istream = new InputStreamReader(
				new BufferedInputStream(
						m_socket.getInputStream()),
						cs);
	}
}
