package tribefire.extension.setup.hiconic.sdk;

@SuppressWarnings("serial")
public class ErrorException extends RuntimeException {

	public ErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorException(String message) {
		super(message);
	}
	

}
