package wwwordz.client;

/**
 * Abstract observer interface.
 * 
 * @author vrls
 *
 */
public interface Observer {
	void update(Observable o, Object arg);
}