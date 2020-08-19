package ae.gov.sdg.paperless.platform.tracing;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    private boolean logRequest;
    private boolean logResponse;
    @SuppressWarnings("unused")
	private String context;
    
    private static LoggingInterceptor interceptor;
    private static Object mutex = new Object();

    private LoggingInterceptor() {
	}

	public static LoggingInterceptor getInstance() {
		LoggingInterceptor result = interceptor;
		if (result == null) {
			synchronized (mutex) {
				result = interceptor;
				if (result == null)
					interceptor = result = new LoggingInterceptor();
			}
		}
		return result;
	}

	/**
	 * @param logRequest the logRequest to set
	 * @return 
	 */
	public LoggingInterceptor setLogRequest(boolean logRequest) {
		this.logRequest = logRequest;
		return interceptor;
	}

	/**
	 * @param logResponse the logResponse to set
	 * @return 
	 */
	public LoggingInterceptor setLogResponse(boolean logResponse) {
		this.logResponse = logResponse;
		return interceptor;
	}

	/**
	 * @param context the context to set
	 * @return 
	 */
	public LoggingInterceptor setContext(String context) {
		this.context = context;
		return interceptor;
	}

	@Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
        if(this.logRequest) {
            traceRequest(request, body);
        }

        final ClientHttpResponse response = execution.execute(request, body);

        if(this.logResponse) {
            traceResponse(response);
        }

        return response;
    }

    private void traceRequest(final HttpRequest request, final byte[] body) {

        // process sensitive data if needed 

        final String reqBody = this.replaceSensitiveData(new String(body, UTF_8));

        // remove the Authorization header
        final Map<String, String> headers = request.getHeaders().toSingleValueMap();
        headers.remove("Authorization");
        final LogMessageBuilder messageBuilder = new LogMessageBuilder().setUri(request.getURI()).setMethod(request.getMethod()).setHeaders(headers);
        if(reqBody!=null && reqBody.length()>1000) {
        	messageBuilder.setRequestBody(reqBody.substring(0,999));
        }
        log.info("Tracing request URI: {} method: {}", request.getURI(), request.getMethod());
        String string = messageBuilder.build().toString();
		log.debug("Tracing request: {}", string);
    }

    private void traceResponse(final ClientHttpResponse response) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), 
                StandardCharsets.UTF_8))) {
            
            final Map<String, String> headers = response.getHeaders().toSingleValueMap();
            final String body = reader.lines().parallel().collect(Collectors.joining("\n"));
            final LogMessageBuilder messageBuilder = new LogMessageBuilder().setHttpStatus(response.getStatusCode()).setHeaders(headers);
            if(body!=null && body.length()>1000) {
            	messageBuilder.setResponseBody(body.substring(0, 999));
            }
            String string = messageBuilder.build().toString();
			log.debug("Tracing response: {}", string);

        } catch(final Exception e) {
            log.error("An error occurred when printing the response: {}", e.getMessage());
        }

    }

    /**
     * A placeholder for subclasses to provide their own sensitive data replacement if needed
     * @param reqBody
     * @return
     */
    protected String replaceSensitiveData(final String reqBody) {
        return reqBody;
    }

}