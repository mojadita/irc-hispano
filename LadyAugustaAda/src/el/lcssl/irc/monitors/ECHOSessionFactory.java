/**
 * 
 */
package el.lcssl.irc.monitors;

import es.lcssl.irc.protocol.Origin;

/**
 * @author lcu
 *
 */
public class ECHOSessionFactory implements SessionFactory<Origin, ECHOSession> {

	@Override
	public ECHOSession newSession(SessionManager<ECHOSession> sessionManager,
			Origin sessionId) {
		return new ECHOSession();
	}

}
