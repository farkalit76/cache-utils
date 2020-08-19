package ae.gov.sdg.paperless.platform.exceptions;

import java.io.IOException;

/**
 * @author omerio
 *
 */
public class ScreenException extends IOException {

	private static final long serialVersionUID = 8042130918350224268L;

	public ScreenException() {
    }

    /**
     * @param message
     */
    public ScreenException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ScreenException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ScreenException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
