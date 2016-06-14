/**
 * 
 */
package el.lcssl.irc.monitors;

/**
 * @author lcu
 *
 */
public interface SessionFactory<K extends Comparable<K>, S extends Session<S>> {
	S newSession(SessionManager<S> sessionManager, K sessionId);
}
