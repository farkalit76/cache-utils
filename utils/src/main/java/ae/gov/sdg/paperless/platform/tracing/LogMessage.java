package ae.gov.sdg.paperless.platform.tracing;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import ae.gov.sdg.paperless.platform.util.JsonUtil;

/**
 * @author c_chandra.bommise
 * <p>
 * Message for logging the request and response.
 */
public class LogMessage {

    private URI uri;

    private HttpMethod method;

    private Map<String, String> headers;

    private String requestBody;

    private String responseBody;

    private HttpStatus httpStatus;

    public LogMessage() {
        super();
    }

    public LogMessage(final URI uri, final HttpMethod method, final Map<String, String> headers, final String requestBody, final String responseBody,
                      final HttpStatus httpStatus) {
        super();
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.httpStatus = httpStatus;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(final URI uri) {
        this.uri = uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(final HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(final String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
    	try {
			return JsonUtil.toJson(this);
		} catch (final Exception e) {
    		return EMPTY;
		}
    }
}