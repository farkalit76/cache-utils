package ae.gov.sdg.paperless.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author c_chandra.bommise
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoDataFoundException extends Exception {

	private static final long serialVersionUID = -2106626353792339054L;

	public NoDataFoundException() {
        super();
    }

    public NoDataFoundException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoDataFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoDataFoundException(final String message) {
        super(message);
    }

    public NoDataFoundException(final Throwable cause) {
        super(cause);
    }

}
