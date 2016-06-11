/**
 * 
 */
package el.lcssl.irc.monitors;

import java.util.Properties;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;

/**
 * @author lcu
 *
 */
public class COMMANDSession implements Session<COMMANDSession> {

	Properties m_properties;
	
	public COMMANDSession(Properties properties) {
		m_properties = properties;
	}
	
	private static enum Commands {
		QUIT(IRCCode.QUIT),
		JOIN(IRCCode.JOIN),
		PART(IRCCode.PART),
		;
		
		public final IRCCode code;
		
		Commands(IRCCode code) {
			this.code = code;
		}
	}
	
	@Override
	public int run(SessionManager<COMMANDSession> sessionManager) {
		try {
			for (;;) {
				Event<Monitor, IRCCode, IRCMessage> event = sessionManager.getEvent();
				Monitor monitor = event.getSource();
				IRCMessage message = event.getMessage();
				Origin origin = message.getOrigin();
				if (	   message.getCode() != IRCCode.PRIVMSG
						|| message.getParams().size() != 2)
					continue;
				String[] cmd = message.getParams().get(1).split("\\s+", 3);
				Commands theCommand;
				try {
					theCommand = Commands.valueOf(cmd[0].toUpperCase());
				} catch (Exception e) {
					monitor.getIrcSAP().addMessage(new IRCMessage(
							IRCCode.NOTICE, 
							origin.getNick(), 
							cmd[0] + ": command not found"));
					continue;
				}
				if (cmd.length < 2) {
					monitor.getIrcSAP().addMessage(new IRCMessage(
							IRCCode.NOTICE, 
							origin.getNick(), 
							cmd[0] + ": need at least one parameter"));
					continue;
				}
				IRCMessage newMessage = new IRCMessage(theCommand.code);
				switch (theCommand) {
				case JOIN: case PART:
					switch(cmd.length) {
					case 3:
						newMessage.getParams().add(cmd[1]);
						newMessage.getParams().add(cmd[2]);
						break;
					case 2:
						newMessage.getParams().add(cmd[1]);
						break;
					}
					break;
				case QUIT:
					StringBuilder sb = new StringBuilder();
					switch (cmd.length) {
					case 2: newMessage.getParams().add(cmd[1]); break;
					case 3: newMessage.getParams().add(cmd[1] + " " + cmd[2]); break;
					}
				}
				monitor.getIrcSAP().addMessage(new IRCMessage(
						IRCCode.PRIVMSG, 
						origin.getNick(), 
						"Executing " + newMessage));
				monitor.getIrcSAP().addMessage(newMessage);
				if (theCommand == Commands.QUIT)
					return 0;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
