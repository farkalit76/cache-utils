package ae.gov.sdg.paperless.platform.exceptions;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.ROUTE_INSTANTIATION_ERR_CODE;

/**
 * @author c_chandra.bommise
 * 
 * Exception for handling routing framework issues.
 *
 */
public class ServiceInstantiationException extends RuntimeException {

	private static final long serialVersionUID = -909688083831296767L;

	private String errorCode = ROUTE_INSTANTIATION_ERR_CODE;

    private Severity errorSeverity = Severity.CRITICAL;

    public ServiceInstantiationException() {
        super();
    }

    public ServiceInstantiationException(final String message, final Throwable cause, final boolean enableSuppression,
                                   final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceInstantiationException(final String message) {
        super(message);
    }

    public ServiceInstantiationException(final Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ServiceInstantiationException setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Severity getErrorSeverity() {
        return errorSeverity;
    }

    public ServiceInstantiationException setErrorSeverity(final Severity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }

}
