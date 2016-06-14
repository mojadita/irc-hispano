/**
 * 
 */
package el.lcssl.irc.monitors;

import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * @author lcu
 *
 */
public interface SessionManager<S extends Session<S>> {
	Event<Monitor, IRCCode, IRCMessage> getEvent() 
			throws InterruptedException;

	Event<Monitor, IRCCode, IRCMessage> getEvent(long timespec, TimeUnit unit) 
			throws InterruptedException;

	Monitor getMonitor();
}
