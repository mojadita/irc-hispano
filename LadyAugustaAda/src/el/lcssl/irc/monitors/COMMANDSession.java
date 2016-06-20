/**
 * 
 */
package el.lcssl.irc.monitors;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;
import es.lcssl.irc.protocol.Origin;
import es.lcssl.irc.transactions.IllegalStateException;
import es.lcssl.irc.transactions.TransactionFactory;
import es.lcssl.irc.transactions.TransactionFactory.Transaction;

/**
 * @author lcu
 *
 */
public class COMMANDSession implements Session<COMMANDSession> {
	
	public static final String PROPERTY_TIMEOUT = "irc.command.timeout";

	private Origin 		m_origin;
	private Properties 	m_properties;
	private long		m_timeout;
	
	public COMMANDSession(Origin origin, Properties properties) {
		m_origin 	 = origin;
		m_properties = properties;
		String to = m_properties.getProperty(PROPERTY_TIMEOUT, "30");
		m_timeout = Long.parseLong(to);
	}
	
	private static enum Commands {
		QUIT(IRCCode.QUIT, 1),
		JOIN(IRCCode.JOIN, 2),
		PART(IRCCode.PART, 2),
		WHOIS(IRCCode.WHOIS, 2),
		;
		
		public final IRCCode code;
		public final int numArgs;
		
		Commands(IRCCode code, int numArgs) {
			this.code 	 = code;
			this.numArgs = numArgs;
		}
	}
	
	@Override
	public int run(SessionManager<COMMANDSession> sessionManager) {
		try {
			Event<Monitor, IRCCode, IRCMessage> event;
			while ((event = sessionManager.getEvent(m_timeout, TimeUnit.SECONDS)) 
					!= null) {
				Monitor monitor = event.getSource();
				IRCMessage message = event.getMessage();
				Origin origin = message.getOrigin();
				if (	   message.getCode() != IRCCode.PRIVMSG
						|| message.getParams().size() != 2)
					continue;
				String[] cmd = message.getParams().get(1).split("\\s+", 2);
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
				
				IRCMessage newMessage;
				if (cmd.length > 1)
					newMessage = new IRCMessage(
						theCommand.code, 
						cmd[1].split(" +", theCommand.numArgs));
				else
					newMessage = new IRCMessage(theCommand.code);
				
				monitor.getIrcSAP().addMessage(new IRCMessage(
						IRCCode.PRIVMSG, 
						origin.getNick(), 
						"Sending [" + newMessage + "]"));
				Transaction tx = monitor.getIrcSAP().getTransactionFactory().newTransaction(newMessage);
				tx.execute();
				Collection<Event<TransactionFactory, IRCCode, IRCMessage>> events = tx.getEvents();
				for (Event<TransactionFactory,IRCCode,IRCMessage> ev: events) {
					monitor.getIrcSAP().addMessage(
							new IRCMessage(
									IRCCode.PRIVMSG, 
									origin.getNick(), 
									"[" + ev.getTimestamp() + "]: " + ev.getMessage()));
				}
			}
			sessionManager.getMonitor().getIrcSAP().addMessage(new IRCMessage(IRCCode.NOTICE,
					m_origin.getNick(), "TIMEOUT!!!"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
