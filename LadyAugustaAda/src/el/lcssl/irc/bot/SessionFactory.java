/**
 * 
 */
package el.lcssl.irc.bot;

/**
 * @author lcu
 *
 */
public interface SessionFactory<S extends Session<S>> {
	S newSession(SessionManager<S> sessionManager, String sessionId);
}
