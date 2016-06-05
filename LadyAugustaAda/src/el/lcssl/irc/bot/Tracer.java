package el.lcssl.irc.bot;

import java.util.Properties;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

public class Tracer implements EventListener<Monitor,IRCCode,IRCMessage> {
	
	public static final String PROPERTY_COLORFORMAT = "irc.client.colorformat";

	static long lastMessage;
	String m_label;
	Properties m_props;

	public Tracer(Properties props, String label) {
		m_label = label;
	}
	
	public static String color(int num, String s) {
		String format = TestIrcSAPBot.props.getProperty(PROPERTY_COLORFORMAT);
		if (format != null)
			return String.format(format, num, s);
		else return s;
	}
	
	@Override
	public void process(Monitor source,
			Event<Monitor, IRCCode, IRCMessage> event) {
		synchronized(this.getClass()) {
			IRCMessage m = event.getMessage();
			long timestamp = event.getTimestamp();
			if (lastMessage == 0) lastMessage = timestamp;
			long lap = timestamp - lastMessage;
			System.out.println(
					String.format(color(36, "%5d.%03d"), lap/1000, lap%1000)
					+ m_label
					+ (m.getCode().isReply() 
							? color(m.getCode().isError() ? 31 : 32, m.getCode().name()) + ": " 
							: "")
					+ m);
			lastMessage = timestamp;
		}
	}
}