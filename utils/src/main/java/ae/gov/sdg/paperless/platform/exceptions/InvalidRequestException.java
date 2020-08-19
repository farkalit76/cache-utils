package ae.gov.sdg.paperless.platform.exceptions;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.INVALID_REQUEST_ERR_CODE;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author c_chandra.bommise
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {

    private static final long serialVersionUID = 2451574966333144759L;

    private String errorCode = INVALID_REQUEST_ERR_CODE;

    private Severity errorSeverity = Severity.HIGH;

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(final String message, final Throwable cause, final boolean enableSuppression,
                                   final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(final String message) {
        super(message);
    }

    public InvalidRequestException(final Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public InvalidRequestException setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Severity getErrorSeverity() {
        return errorSeverity;
    }

    public InvalidRequestException setErrorSeverity(final Severity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }
}
