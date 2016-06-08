/**
 * 
 */
package el.lcssl.irc.monitors;



/**
 * @author lcu
 *
 */
public interface Session<S extends Session<S>> {
	int run(SessionManager<S> sessionManager);
}
