package ae.gov.sdg.paperless.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author c_chandra.bommise
 * 
 * Parent handler for populating exception messages.
 *
 */
public abstract class AbstractExceptionHandler extends ResponseEntityExceptionHandler {
	
	protected ResponseEntity<Object> buildResponseEntity(final ApiError apiError, final HttpStatus status) {
		return new ResponseEntity<>(apiError, status);
	}
	
	protected ApiError populateErrorMessage(final Exception ex, final String errorCode, final HttpStatus status) {
		final ApiError apiError = new ApiError(errorCode);
		apiError.setMessage(ex.getMessage());
		apiError.setStatus(status.value());
		apiError.setError(status.name());
		return apiError;
	}
	
	
	protected ApiError populateErrorMessage(final Exception ex, final String errorCode, final Severity errorSeverity, final HttpStatus status) {
		final ApiError apiError = new ApiError(errorCode);
		apiError.setErrorSeverity(errorSeverity.name());
		apiError.setMessage(ex.getMessage());
		apiError.setStatus(status.value());
		apiError.setError(status.name());
		return apiError;
	}
	
	protected ApiError populateErrorMessage(final String message, final String errorCode, final Severity errorSeverity, final HttpStatus status) {
		final ApiError apiError = new ApiError(errorCode);
		apiError.setErrorSeverity(errorSeverity.name());
		apiError.setMessage(message);
		apiError.setStatus(status.value());
		apiError.setError(status.name());
		return apiError;
	}
	
}