/**
 *
 */
package ae.gov.sdg.paperless.platform.exceptions;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.CUSTOM_EXCEPTION_ERR_CODE;

/**
 * @author swetabh raj
 *
 */
public class CustomException extends RuntimeException {

    private String errorCode = CUSTOM_EXCEPTION_ERR_CODE;

    private Severity errorSeverity = Severity.HIGH;

    private static final long serialVersionUID = 4674804468540704271L;

    /**
     *
     */
    public CustomException() {
        super();
    }

    /**
     * @param message
     */
    public CustomException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CustomException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public CustomException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public CustomException(final String message, final Throwable cause, final boolean enableSuppression,
                           final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public CustomException setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Severity getErrorSeverity() {
        return errorSeverity;
    }

    public CustomException setErrorSeverity(final Severity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }
}
