/**
 * 
 */
package el.lcssl.irc.bot;

/**
 * @author lcu
 *
 */
public interface SessionFactory<K extends Comparable<K>, S extends Session<S>> {
	S newSession(SessionManager<S> sessionManager, K sessionId);
}
