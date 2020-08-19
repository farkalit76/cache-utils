/**
 * 
 */
package ae.gov.sdg.paperless.platform.exceptions;

/**
 * @author c_farkalit.usman
 *
 */
public class DNRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5808382659725676781L;

	/**
	 * 
	 */
	public DNRuntimeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DNRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DNRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DNRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DNRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
