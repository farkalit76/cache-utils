package ae.gov.sdg.paperless.platform.tracing;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_DN_APP_VERSION;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_DN_PLATFORM;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_SESSION_ID;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_TRACE_ID;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ae.gov.sdg.paperless.platform.common.PlatformConfig;

/**
 * @author c_chandra.bommise
 * 
 * Add the tracing request headers to MDC for logging.
 *
 */
@Component
public class LogTracingFilter implements Filter {
	
	private static final String LOG_SPAN_ID = "spanId";
	
	@Autowired
    private PlatformConfig platformConfig;
	
	@Override
    public void init(final FilterConfig fc) throws ServletException {
		//will implement as needed
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {

		if (req instanceof HttpServletRequest) {
			final HttpServletRequest request = (HttpServletRequest) req;
			final String spanId = new StringBuilder(platformConfig.getMsName()).append(RandomStringUtils.randomAlphabetic(6)).toString();
	    	MDC.put(LOG_SPAN_ID, spanId);
			final String sessionId = request.getHeader(X_SESSION_ID);
			final String requestCid = request.getHeader(X_TRACE_ID);
			final String appVersion = request.getHeader(X_DN_APP_VERSION);
			final String platform = request.getHeader(X_DN_PLATFORM);
			// add cid to the MDC
			MDC.put(X_TRACE_ID, requestCid);
			MDC.put(X_SESSION_ID, sessionId);
			MDC.put(X_DN_APP_VERSION, appVersion);
			MDC.put(X_DN_PLATFORM, platform);
		}

        try {
			// call filter(s) upstream for the real processing of the request
            chain.doFilter(req, res);
        } finally {
			// remove the cid from the MDC
            MDC.remove(X_TRACE_ID);
            MDC.remove(X_SESSION_ID);
            MDC.remove(LOG_SPAN_ID);
            MDC.remove(X_DN_APP_VERSION);
            MDC.remove(X_DN_PLATFORM);
        }
    }

    @Override
    public void destroy() {
    	//will implement as needed
    }
    
}