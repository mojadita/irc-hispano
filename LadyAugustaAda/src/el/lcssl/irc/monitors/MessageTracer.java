package el.lcssl.irc.monitors;

import java.util.Properties;

import es.lcssl.irc.protocol.Event;
import es.lcssl.irc.protocol.EventListener;
import es.lcssl.irc.protocol.IRCCode;
import es.lcssl.irc.protocol.IRCMessage;
import es.lcssl.irc.protocol.IrcSAP.Monitor;

public class MessageTracer implements EventListener<Monitor,IRCCode,IRCMessage> {
	
	public static final String PROPERTY_COLORFORMAT = "irc.client.colorformat";

	static long lastMessage;
	String m_label;
	Properties m_props;

	public MessageTracer(Properties props, String label) {
		m_label = label;
		m_props = props;
	}
	
	public String color(int num, String s) {
		String format = m_props.getProperty(PROPERTY_COLORFORMAT);
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
			String fmt = "%5d.%03d";
			if (lap < 0) {
				lap = -lap;
				fmt = "-" + fmt;
			}
			System.out.println(
					String.format(color(36, fmt), lap/1000, lap%1000)
					+ color(m_label.hashCode()%7 + 31, m_label)
					+ (m.getCode().isReply() 
							? color(m.getCode().isError() ? 31 : 32, m.getCode().name()) + ": " 
							: "")
					+ m);
			lastMessage = timestamp;
		}
	}
}