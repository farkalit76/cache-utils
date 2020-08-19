package ae.gov.sdg.paperless.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author c_chandra.bommise
 *
 */
@ResponseStatus(HttpStatus.OK)
public class BusinessValidationException extends RuntimeException {

	private static final long serialVersionUID = 2451574966333144759L;

    private BusinessValidationExceptionContext exceptionContext;

	/**
	 *
	 */
	public BusinessValidationException() {
		super();
	}

	/**
	 * @param message
	 */
	public BusinessValidationException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BusinessValidationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BusinessValidationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BusinessValidationException(final String message, final Throwable cause, final boolean enableSuppression,
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
	public BusinessValidationException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace, final BusinessValidationExceptionContext exceptionContext) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.exceptionContext = exceptionContext;
	}


	/**
	 * @param message
	 * @param cause
	 * @param exceptionContext
	 */
	public BusinessValidationException(final String message, final Throwable cause, final BusinessValidationExceptionContext exceptionContext) {
		super(message, cause);
		this.exceptionContext = exceptionContext;
	}

	public BusinessValidationException(final String message, final BusinessValidationExceptionContext context) {
	    this(message, null, context);
    }

	public BusinessValidationException(final BusinessValidationExceptionContext exceptionContext) {
		super();
		this.exceptionContext = exceptionContext;
	}

	public BusinessValidationExceptionContext getExceptionContext() {
		return exceptionContext;
	}

	public void setExceptionContext(final BusinessValidationExceptionContext exceptionContext) {
		this.exceptionContext = exceptionContext;
	}
}
