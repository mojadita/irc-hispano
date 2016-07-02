/**
 * 
 */
package es.lcssl.irc.monitors;

import java.util.Properties;

import es.lcssl.irc.protocol.Origin;
import es.lcssl.sessions.SessionFactory;
import es.lcssl.sessions.SessionManager;

/**
 * @author lcu
 *
 */
public class COMMANDSessionFactory implements SessionFactory<Origin, COMMANDSession> {
	
	public Properties m_properties;
	
	public COMMANDSessionFactory(Properties properties) {
		m_properties = properties;
	}

	@Override
	public COMMANDSession newSession(SessionManager<COMMANDSession> sessionManager,
			Origin sessionId) {
		return new COMMANDSession(sessionId, m_properties);
	}

}
