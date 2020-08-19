package ae.gov.sdg.paperless.platform.exceptions;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.EXTERNAL_API_ERR_CODE;

public class ExternalApiException extends RuntimeException {

    private static final long serialVersionUID = 1622763433495491520L;

    private String errorCode = EXTERNAL_API_ERR_CODE;

    private Severity errorSeverity = Severity.CRITICAL;

    private String customMessage;

    public ExternalApiException() {
        super();
    }

    /**
     * @param message
     */
    public ExternalApiException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ExternalApiException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ExternalApiException(final String message, final Throwable cause) {
        super(message, cause);
    }


    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ExternalApiException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ExternalApiException setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Severity getErrorSeverity() {
        return errorSeverity;
    }

    public ExternalApiException setErrorSeverity(final Severity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public ExternalApiException setCustomMessage(final String customMessage) {
        this.customMessage = customMessage;
        return this;
    }
}
