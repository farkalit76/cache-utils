/**
 * 
 */
package ae.gov.sdg.paperless.platform.exceptions;

/**
 * @author omerio
 *
 */
public class JourneyCompleteException extends Exception {

    private static final long serialVersionUID = 2367819897682046908L;

    public JourneyCompleteException() {
        super();
    }

    public JourneyCompleteException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JourneyCompleteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JourneyCompleteException(final String message) {
        super(message);
    }

    public JourneyCompleteException(final Throwable cause) {
        super(cause);
    }

}
