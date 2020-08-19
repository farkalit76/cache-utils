package ae.gov.sdg.paperless.platform.exceptions;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.RESOURCE_LOADER_ERR_CODE;

/**
 * @author c_chandra.bommise
 *
 */
public class ResourceLoaderException extends RuntimeException {

	private static final long serialVersionUID = 2534988081202416999L;

	private String errorCode = RESOURCE_LOADER_ERR_CODE;

    private Severity errorSeverity = Severity.CRITICAL;

	public ResourceLoaderException() {
        super();
    }

    public ResourceLoaderException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResourceLoaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ResourceLoaderException(final String message) {
        super(message);
    }

    public ResourceLoaderException(final Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ResourceLoaderException setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Severity getErrorSeverity() {
        return errorSeverity;
    }

    public ResourceLoaderException setErrorSeverity(final Severity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }
}
