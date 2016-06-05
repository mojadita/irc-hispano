/**
 * 
 */
package el.lcssl.irc.bot;

/**
 * @author lcu
 *
 */
public class ECHOSessionFactory implements SessionFactory<ECHOSession> {

	@Override
	public ECHOSession newSession(SessionManager<ECHOSession> sessionManager,
			String sessionId) {
		return new ECHOSession();
	}

}
