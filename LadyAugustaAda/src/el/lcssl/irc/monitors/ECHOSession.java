/**
 * 
 */
package el.lcssl.irc.monitors;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

/**
 * @author lcu
 *
 */
public class ECHOSession implements Session<ECHOSession> {

	@Override
	public int run(SessionManager<ECHOSession> sessionManager) {
		try {
			long oldTimestamp = 0;
			Event<Monitor,IRCCode,IRCMessage> event = null; 
			for (int i = 0; i < 10; i++) {
				event = sessionManager.getEvent();
				Monitor monitor = event.getSource();
				long timestamp = event.getTimestamp();
				if (oldTimestamp == 0) oldTimestamp = timestamp;
				long lap = timestamp - oldTimestamp;
				IRCMessage message = event.getMessage();
				String to = message.getOrigin().getNick();
				monitor.getIrcSAP().addMessage(new IRCMessage(
						IRCCode.PRIVMSG, 
						to,
						">>>(" + i + ", " + String.format("%d.%03d", lap/1000, lap%1000) + "): " + message.getParams().get(1)));
				oldTimestamp = timestamp;
			}
			if (event != null) {
				event.getSource().getIrcSAP().addMessage(new IRCMessage(
						IRCCode.NOTICE, 
						event.getMessage().getOrigin().getNick(),
						">>> SESSION FINISHED " + event.getMessage().getOrigin()));
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
