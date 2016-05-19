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
	
	public static final String PROPERTY_NICK = "user.nick";
	public static final String PROPERTY_IDENT = "user.ident";
	public static final String PROPERTY_NAME = "user.name";
	public static final String PROPERTY_MODES = "user.modes";
	public static final String PROPERTY_PASSWORD = "user.password";
	
	
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
