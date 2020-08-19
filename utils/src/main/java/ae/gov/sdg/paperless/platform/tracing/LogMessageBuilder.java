package ae.gov.sdg.paperless.platform.tracing;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 * @author c_chandra.bommise
 * 
 * Message builder for logging the request and response.
 *
 */
public class LogMessageBuilder {
	
	private URI uri;
	
	private HttpMethod method;
	
	private Map<String, String> headers;
	
	private String requestBody;
	
	private String responseBody;
	
	private HttpStatus httpStatus;
	
	public LogMessageBuilder() {
		super();
	}

	public LogMessageBuilder(final URI uri, final HttpMethod method, final Map<String, String> headers, final String requestBody,
			final String responseBody, final HttpStatus httpStatus) {
		super();
		this.uri = uri;
		this.method = method;
		this.headers = headers;
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.httpStatus = httpStatus;
	}

	public LogMessageBuilder setUri(final URI uri) {
		this.uri = uri;
		return this;

	}

	public LogMessageBuilder setMethod(final HttpMethod httpMethod) {
		this.method = httpMethod;
		return this;

	}

	public LogMessageBuilder setHeaders(final Map<String, String> headers) {
		this.headers = headers;
		return this;

	}

	public LogMessageBuilder setRequestBody(final String requestBody) {
		this.requestBody = requestBody;
		return this;

	}

	public LogMessageBuilder setResponseBody(final String responseBody) {
		this.responseBody = responseBody;
		return this;

	}

	public LogMessageBuilder setHttpStatus(final HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
		return this;
	}


	public LogMessage build() {
		return new LogMessage(uri, method, headers, requestBody, responseBody, httpStatus);
	}

}
