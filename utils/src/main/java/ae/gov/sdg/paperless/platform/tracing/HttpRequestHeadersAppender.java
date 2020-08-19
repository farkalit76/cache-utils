package ae.gov.sdg.paperless.platform.tracing;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class HttpRequestHeadersAppender implements ClientHttpRequestInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHeadersAppender.class);
	private final List<String> appendHeaders;

	public HttpRequestHeadersAppender(final List<String> appendHeaders) {
		this.appendHeaders = appendHeaders;
	}

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) throws IOException {
		long start = System.currentTimeMillis();
		final HttpHeaders headers = request.getHeaders();
		appendHeaders.forEach(appendHeader -> headers.add(appendHeader, MDC.get(appendHeader)));
		ClientHttpResponse response = execution.execute(request, body);
		LOGGER.info("Outbound API {} {} execution time {} ms", request.getURI(), request.getMethod(),
				System.currentTimeMillis() - start);
		return response;
	}
}