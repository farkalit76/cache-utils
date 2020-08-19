package ae.gov.sdg.paperless.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author saddam hussain
 *
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserAuthenticationFailedException extends Exception {


	private static final long serialVersionUID = 7336855691430140702L;

	public UserAuthenticationFailedException() {
        super();
    }

    public UserAuthenticationFailedException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserAuthenticationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAuthenticationFailedException(final String message) {
        super(message);
    }

    public UserAuthenticationFailedException(final Throwable cause) {
        super(cause);
    }

}
