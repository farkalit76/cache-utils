package ae.gov.sdg.paperless.platform.exceptions;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.BUSINESS_VALIDATION_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.BUSINESS_VLD_ERROR_DISMISS_TEMPLATE_SCREEN;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.CUSTOM_EXCEPTION_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.ERROR_SEVERITY;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.EXCEPTION_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.EXTERNAL_API_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.GENERIC_ERROR_TEMPLATE_SCREEN;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.GENERIC_SCREEN_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.LOG_ERROR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.NOT_FOUND_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.NO_DATA_FOUND_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.REFERENCE_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.SCREEN_EXCEPTION_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.SOCKET_TIMEOUT_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.UNAUTHORIZED_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_TRACE_ID;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.EXTERNAL_API_RESOURCE_ERR_CODE;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import ae.gov.sdg.paperless.platform.common.IScreenService;
import ae.gov.sdg.paperless.platform.common.model.components.types.LangType;

/**
 * @author c_chandra.bommise
 * 
 *         Exception handler for handling different types exceptions and LOG the
 *         exceptions.
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends AbstractExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Autowired
	@Qualifier("screenParserService")
	private IScreenService screenService;

	/**
	 * Handle GenericException and prepare error screen.
	 *
	 * @param ex
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<String> genericExceptionHandler(final GenericException ex, final WebRequest request) throws IOException {
		MDC.put(LOG_ERROR_CODE, GENERIC_SCREEN_ERR_CODE);
		if (ex.getExceptionContext() != null) {
			MDC.put(ERROR_SEVERITY, ex.getExceptionContext().getSeverity());
		} else {
			MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		}
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(LOG_ERROR_CODE);
		MDC.remove(ERROR_SEVERITY);
		String screens;
		HttpHeaders headers = null;
		final GenericExceptionContext context = ex.getExceptionContext();
		if (context != null) {
			if (context.getDictionary() != null) {
				context.getDictionary().put(REFERENCE_CODE, MDC.get(X_TRACE_ID));
			} else {
				final Map<String, Object> map = new HashMap<>();
				map.put(REFERENCE_CODE, MDC.get(X_TRACE_ID));
				context.setDictionary(map);
			}
			screens = this.screenService.getAndPopulateScreens(context.getFilename(), context.getDictionary(),
					(context.getLang() != null ? context.getLang() : LangType.en), context.getProcessId(),
					context.getJourney());
			headers = context.getHeaders();
		} else {
			final Map<String, Object> map = new HashMap<>();
			map.put(REFERENCE_CODE, MDC.get(X_TRACE_ID));
			screens = this.screenService.getAndPopulateScreens(GENERIC_ERROR_TEMPLATE_SCREEN, map, LangType.en, "",
					null);
		}
		if (headers == null) {
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		}
		return new ResponseEntity<>(screens, headers, HttpStatus.OK);
	}

	/**
	 * Handle UserAuthenticationFailedException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ExternalApiException.class)
	public ResponseEntity<ApiError> entityNotRespondingExceptionHandler(final ExternalApiException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, ex.getErrorCode());
		MDC.put(ERROR_SEVERITY, ex.getErrorSeverity());
		LOG.error(EXTERNAL_API_ERR_CODE, ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, ex.getErrorCode(), ex.getErrorSeverity(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle UserAuthenticationFailedException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(UserAuthenticationFailedException.class)
	public ResponseEntity<ApiError> authenticationExceptionHandler(final UserAuthenticationFailedException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, UNAUTHORIZED_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.HIGH);
		LOG.error(UNAUTHORIZED_ERR_CODE, ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, UNAUTHORIZED_ERR_CODE, Severity.HIGH, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handle CustomException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiError> customExceptionHandler(final CustomException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, ex.getErrorCode());
		MDC.put(ERROR_SEVERITY, ex.getErrorSeverity());
		LOG.error(ex.getMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, CUSTOM_EXCEPTION_ERR_CODE, ex.getErrorSeverity(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle CustomException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> exceptionHandler(final Exception ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle ScreenException, JourneyCompleteException, InvalidTaskException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ScreenException.class, JourneyCompleteException.class, InvalidTaskException.class })
	public ResponseEntity<ApiError> screenExceptionHandler(final Exception ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, SCREEN_EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.HIGH);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, SCREEN_EXCEPTION_ERR_CODE, Severity.HIGH,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle BusinessValidationException and prepare screen.
	 *
	 * @param ex
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(BusinessValidationException.class)
	public ResponseEntity<String> businessValidationExceptionHandler(final BusinessValidationException ex, final WebRequest request)
			throws IOException {
		MDC.put(LOG_ERROR_CODE, BUSINESS_VALIDATION_ERR_CODE);
		if (ex.getExceptionContext() != null) {
			MDC.put(ERROR_SEVERITY, ex.getExceptionContext().getSeverity());
			if (CollectionUtils.isEmpty(ex.getExceptionContext().getDictionary())) {
				LOG.error(ex.getLocalizedMessage(), ex);
			} else {
				LOG.error("Error message in business validation - {} ",ex.getExceptionContext().getDictionary(), ex);
			}
		} else {
			MDC.put(ERROR_SEVERITY, Severity.LOW);
			LOG.error(ex.getLocalizedMessage(), ex);
		}

		MDC.remove(LOG_ERROR_CODE);
		MDC.remove(ERROR_SEVERITY);
		String screens;
		HttpHeaders headers = null;
		final BusinessValidationExceptionContext context = ex.getExceptionContext();
		if (context != null) {
			if (context.getDictionary() != null) {
				context.getDictionary().put(REFERENCE_CODE, MDC.get(X_TRACE_ID));
			} else {
				final Map<String, Object> map = new HashMap<>();
				map.put(REFERENCE_CODE, MDC.get(X_TRACE_ID));
				context.setDictionary(map);
			}
			screens = this.screenService.getAndPopulateScreens(
					context.getFilename() != null ? context.getFilename() : BUSINESS_VLD_ERROR_DISMISS_TEMPLATE_SCREEN,
					context.getDictionary(), (context.getLang() != null ? context.getLang() : LangType.en),
					context.getProcessId(), context.getJourney());
			headers = context.getHeaders();
		} else {
			final Map<String, Object> map = new HashMap<>();
			map.put(REFERENCE_CODE, MDC.get(X_TRACE_ID));
			screens = this.screenService.getAndPopulateScreens(BUSINESS_VLD_ERROR_DISMISS_TEMPLATE_SCREEN, map,
					LangType.en, "", null);
		}
		if (headers == null) {
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		}
		return new ResponseEntity<>(screens, headers, HttpStatus.OK);
	}

	/**
	 * Handle InvalidRequestException
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ApiError> invalidRequestExceptionHandler(final InvalidRequestException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, ex.getErrorCode());
		MDC.put(ERROR_SEVERITY, ex.getErrorSeverity());
		LOG.error(ex.getMessage(), ex);
		final ApiError apiError = populateErrorMessage(ex, ex.getErrorCode(), ex.getErrorSeverity(), HttpStatus.BAD_REQUEST);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle NoDataFoundException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<ApiError> noDataFoundExceptionHandler(final NoDataFoundException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, NO_DATA_FOUND_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.LOW);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, NO_DATA_FOUND_ERR_CODE, Severity.LOW,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle SocketTimeoutException
	 *
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(SocketTimeoutException.class)
	public ResponseEntity<ApiError> entityNotRespondingExceptionHandler(final SocketTimeoutException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, SOCKET_TIMEOUT_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.CRITICAL);
		LOG.error(SOCKET_TIMEOUT_ERR_CODE, ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, SOCKET_TIMEOUT_ERR_CODE, Severity.CRITICAL,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, NOT_FOUND_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.LOW);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, NOT_FOUND_ERR_CODE, Severity.LOW, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.LOW);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.LOW, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.LOW);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.LOW,
				HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.LOW);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.LOW, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.HIGH);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.HIGH, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(final HttpMessageNotWritableException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.HIGH);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.HIGH,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Customize the response for HttpRequestMethodNotSupportedException.
	 * <p>
	 * This method logs a warning, sets the "Allow" header, and delegates to
	 * {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM,
				HttpStatus.METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(apiError, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * Customize the response for HttpMediaTypeNotAcceptableException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(final HttpMediaTypeNotAcceptableException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.LOW, HttpStatus.NOT_ACCEPTABLE);
		return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
	}

	/**
	 * Customize the response for MissingPathVariableException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 * @since 4.2
	 */
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(final MissingPathVariableException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Customize the response for ServletRequestBindingException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Customize the response for ConversionNotSupportedException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(final ConversionNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Customize the response for TypeMismatchException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Customize the response for MissingServletRequestPartException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Customize the response for BindException.
	 * <p>
	 * This method delegates to {@link #handleExceptionInternal}.
	 *
	 * @param ex      the exception
	 * @param headers the headers to be written to the response
	 * @param status  the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {

		MDC.put(LOG_ERROR_CODE, EXCEPTION_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.MEDIUM);
		LOG.error(ex.getLocalizedMessage(), ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXCEPTION_ERR_CODE, Severity.MEDIUM, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Resource access exception handler.
	 *
	 * @param ex the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<ApiError> resourceAccessExceptionHandler(final RestClientException ex, final WebRequest request) {
		MDC.put(LOG_ERROR_CODE, EXTERNAL_API_RESOURCE_ERR_CODE);
		MDC.put(ERROR_SEVERITY, Severity.CRITICAL);
		LOG.error(EXTERNAL_API_RESOURCE_ERR_CODE, ex);
		MDC.remove(ERROR_SEVERITY);
		MDC.remove(LOG_ERROR_CODE);
		final ApiError apiError = populateErrorMessage(ex, EXTERNAL_API_RESOURCE_ERR_CODE, Severity.CRITICAL,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}