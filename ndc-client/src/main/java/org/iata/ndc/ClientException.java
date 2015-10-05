package org.iata.ndc;

/**
 * Indicates serious error in the client.
 *
 */
public class ClientException extends RuntimeException {
	private static final long serialVersionUID = 3110431677701637155L;

	public ClientException() {
		super();
	}
	
	public ClientException(Throwable cause) {
		super(cause);
	}
	
	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
