package ae.gov.sdg.paperless.platform.exceptions;

import ae.gov.sdg.paperless.platform.common.PlatformConstants;

/**
 * The Class MethodNotInUseException.
 * 
 * @author c_vijendra.singh
 */
public class MethodNotInUseException extends RuntimeException {

	private static final long serialVersionUID = 2534988081202416999L;

	private String errorCode = PlatformConstants.METHOD_NOT_IN_ERR_CODE;

    private Severity errorSeverity = Severity.CRITICAL;

	/**
	 * Instantiates a new method not in use exception.
	 */
	public MethodNotInUseException() {
        super();
    }

    /**
     * Instantiates a new method not in use exception.
     *
     * @param message the message
     * @param cause the cause
     * @param enableSuppression the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public MethodNotInUseException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Instantiates a new method not in use exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public MethodNotInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new method not in use exception.
     *
     * @param message the message
     */
    public MethodNotInUseException(String message) {
        super(message);
    }

    /**
     * Instantiates a new method not in use exception.
     *
     * @param cause the cause
     */
    public MethodNotInUseException(Throwable cause) {
        super(cause);
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the error code
     * @return the method not in use exception
     */
    public MethodNotInUseException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * Gets the error severity.
     *
     * @return the error severity
     */
    public Severity getErrorSeverity() {
        return errorSeverity;
    }

    /**
     * Sets the error severity.
     *
     * @param errorSeverity the error severity
     * @return the method not in use exception
     */
    public MethodNotInUseException setErrorSeverity(Severity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }
}
