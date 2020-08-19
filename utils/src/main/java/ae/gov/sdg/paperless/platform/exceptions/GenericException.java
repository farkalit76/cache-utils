/**
 * 
 */
package ae.gov.sdg.paperless.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for handling generic errors.
 * 
 * @author c_chandra.bommise
 *
 */
@ResponseStatus(HttpStatus.OK)
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = -2914244166034821069L;

	private GenericExceptionContext exceptionContext;

	/**
	 * 
	 */
	public GenericException() {
		super();
	}

	/**
	 * @param message
	 */
	public GenericException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GenericException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GenericException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GenericException(final String message, final Throwable cause, final boolean enableSuppression, 
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	
	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 * @param exceptionContext
	 */
	public GenericException(final String message, final Throwable cause, final boolean enableSuppression, 
			final boolean writableStackTrace, final GenericExceptionContext exceptionContext) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.exceptionContext = exceptionContext;
	}

	
	/**
	 * @param message
	 * @param cause
	 * @param exceptionContext
	 */
	public GenericException(final String message, final Throwable cause, final GenericExceptionContext exceptionContext) {
		super(message, cause);
		this.exceptionContext = exceptionContext;
	}

	public GenericException(final GenericExceptionContext exceptionContext) {
		super();
		this.exceptionContext = exceptionContext;
	}

	public GenericExceptionContext getExceptionContext() {
		return exceptionContext;
	}

	public void setExceptionContext(final GenericExceptionContext exceptionContext) {
		this.exceptionContext = exceptionContext;
	}





}