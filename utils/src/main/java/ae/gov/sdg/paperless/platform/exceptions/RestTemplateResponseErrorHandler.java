package ae.gov.sdg.paperless.platform.exceptions;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

@Component("restTemplateResponseErrorHandler")
public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

	@Override
	public void handleError(final ClientHttpResponse response) {
		try {
			super.handleError(response);
		} catch (final HttpClientErrorException ex) {
			throw new ExternalApiException(ex.getMessage(), ex).setErrorSeverity(getSeverity(response))
					.setCustomMessage(ex.getResponseBodyAsString());
		} catch (final HttpServerErrorException ex) {
			throw new ExternalApiException(ex.getMessage(), ex).setErrorSeverity(Severity.HIGH)
					.setCustomMessage(ex.getResponseBodyAsString());
		} catch (final UnknownHttpStatusCodeException ex) {
			throw new ExternalApiException(ex.getMessage(), ex).setErrorSeverity(Severity.CRITICAL)
					.setCustomMessage(ex.getResponseBodyAsString());
		} catch (final IOException ex) {
			throw new ExternalApiException(ex).setCustomMessage(ex.getMessage()).setErrorSeverity(Severity.HIGH);
		}
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	private Severity getSeverity(final ClientHttpResponse response) {
        try {
            if (response.getRawStatusCode() == 404) {
                return Severity.CRITICAL;
            } else if (response.getRawStatusCode() == 401) {
                return Severity.HIGH;
            }
        } catch (final IOException e) {
        	log.error("Severity error: {}",e.getMessage());
        }
        return Severity.CRITICAL;
    }

}
