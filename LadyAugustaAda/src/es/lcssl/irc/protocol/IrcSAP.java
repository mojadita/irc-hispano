/**
 * 
 */
package es.lcssl.irc.protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * IRC Service Access Point.
 * 
 * This class implements the interface to the network for a client IRC connection.
 * The IrcSAP must be started with {@link Thread#start()} method to begin the wheel 
 * rotating.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 */
public class IrcSAP extends Thread {
	
	private enum Status {
		/**
		 * This state is the initial state and the state when
		 * no part of a message has been read.  It is also the
		 * state reached after error recovery.
		 */
		INITIAL, 
		
		/**
		 * This state gets entered when an initial ':' character
		 * has arrived as the first of a message.  It signals an
		 * {@link Origin} field is present and is maintained while
		 * filling the origin address of this message.
		 */
		ORIGIN, 
		
		/**
		 * This state is maintained while reading the blank space that
		 * can be between the {@link Origin} field and the command of
		 * a message.
		 */
		PRE_COMMAND, 
		
		/**
		 * This state is maintained while reading the command part of
		 * a message.  Also when a response code is present for a response
		 * message.
		 */
		COMMAND, 
		
		/**
		 * This state is maintained while reading the blank space present
		 * between command and middle parameters, or the blank space present
		 * between middle parameters/command and final parameter.
		 */
		INTER_PARM, 
		
		/**
		 * This state is held while reading the nonblank part of a middle
		 * parameter.
		 */
		MIDDLE, 
		
		/**
		 * This state is held while reading the final parameter.  Final
		 * parameter is only delimited by the presence of a `\r` or a `\n`
		 * character.
		 */
		FINAL, 
		
		/**
		 * This state marks the end of the IRC message and only a new line
		 * '\n' character is allowed.
		 */
		CR, 
		
		/**
		 * This state is held when encountering a parsing error and is maintained
		 * until a '\n' is received in the input stream.
		 */
		ERROR,
	}
	
	Socket 				m_socket;
	InputStreamReader 	m_istream;
	Properties			m_properties;

	@Override
	public void run() {
		int c;
		Status st = Status.INITIAL;
		StringBuilder origStr = null;
		StringBuilder command = null;
		ArrayList<String> params = null;
		try {
			while ((c = m_istream.read()) != -1) {

				final char ch = (char) c;

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
			e.printStackTrace();
			try {
				System.err.println("...closing socket");
				m_socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
		m_istream = new InputStreamReader(
				new BufferedInputStream(m_socket.getInputStream()));
		m_properties = properties;
	}
}
