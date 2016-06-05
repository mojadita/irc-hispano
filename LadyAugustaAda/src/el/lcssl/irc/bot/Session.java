/**
 * 
 */
package el.lcssl.irc.bot;



/**
 * @author lcu
 *
 */
public interface Session<S extends Session<S>> {
	int run(SessionManager<S> sessionManager);
}
