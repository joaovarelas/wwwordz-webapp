package wwwordz.shared;

import java.io.Serializable;

public class WWWordzException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public WWWordzException() {

	}

	public WWWordzException(String message, Throwable cause) {

	}

	public WWWordzException(String message) {
		// Print message to stderr
		System.err.println(message);
	}

	public WWWordzException(Throwable cause) {

	}
}
