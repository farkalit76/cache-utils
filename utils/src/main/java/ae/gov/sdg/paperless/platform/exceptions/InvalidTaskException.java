/**
 * 
 */
package ae.gov.sdg.paperless.platform.exceptions;

/**
 * @author omerio
 *
 */
public class InvalidTaskException extends Exception {

    private static final long serialVersionUID = 4674804468540704271L;

    /**
     * 
     */
    public InvalidTaskException() {
       super();
    }

    /**
     * @param message
     */
    public InvalidTaskException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidTaskException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidTaskException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidTaskException(final String message, final Throwable cause, final boolean enableSuppression, 
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
