package wwwordz.client;

import java.util.List;

/**
 * Notify observers of call back updates.
 * 
 * @author vrls
 *
 */
public class Observable {
	List<Observer> observers;

	void addObserver(Observer observer) {
		observers.add(observer);
	}

	void notifyObservers(Object arg) {
		for (Observer o : observers)
			o.update(this, arg);
	}

}