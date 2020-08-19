package ae.gov.sdg.paperless.platform.tracing.events;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.LOG_ERROR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.TRACE_LOGGING_ERR_CODE;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_DN_APP_VERSION;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_DN_PLATFORM;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_SESSION_ID;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.X_TRACE_ID;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ae.gov.sdg.paperless.platform.tracing.events.pojo.UserEventsRequest;

/**
 * @author c_chandra.bommise
 * 
 * Asynchronous task execution for logging trace events
 *
 */
@Service
public class EventsExecutorService {

	private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getClass());

	/**
	 * Execute trace requests asynchronously
	 * 
	 * @param traceRequests
	 */
	@Async
	public void createEvent(List<UserEventsRequest> traceRequests) {
        String traceId = MDC.get(X_TRACE_ID);
        String sessionId = MDC.get(X_SESSION_ID);
        final String  appVersion = MDC.get(X_DN_APP_VERSION);
        final String platform = MDC.get(X_DN_PLATFORM);
		traceRequests.forEach(traceRequest -> {
			convertLongTimeToDate(traceRequest);
			logRequest(traceRequest, traceId, sessionId, appVersion, platform);
		});
	}

    private void logRequest(UserEventsRequest traceRequest, String traceId, String sessionId, String appVersion, String platform) {
        if (!StringUtils.isEmpty(traceRequest.getTraceId()) && !StringUtils.isEmpty(traceRequest.getSessionId())) {
            mdcSetContextualSessionTrace(traceRequest.getTraceId(), traceRequest.getSessionId(), appVersion, platform);
        } else {
            mdcSetContextualSessionTrace(traceId, sessionId, appVersion, platform);
        }
        final String string = traceRequest.toString();
		if(traceRequest.isError()) {
            if (StringUtils.isEmpty(traceRequest.getErrorCode())) {
                mdcSetContextualErrorCode(TRACE_LOGGING_ERR_CODE);
            }else {
                mdcSetContextualErrorCode(traceRequest.getErrorCode());
            }
        	log.error(string);
        } else {
            mdcSetContextualErrorCode("");
        	log.info(string);
        }
    }

    private void mdcSetContextualSessionTrace(String traceId, String sessionId, String appVersion, String platform) {
        MDC.put(X_TRACE_ID, traceId);
        MDC.put(X_SESSION_ID, sessionId);
        MDC.put(X_DN_APP_VERSION, appVersion);
        MDC.put(X_DN_PLATFORM, platform);
    }

    private void mdcSetContextualErrorCode(String errorCode) {
        MDC.put(LOG_ERROR_CODE, errorCode);
    }

	/**
	 * Convert time to UTC date.
	 *
	 * @param traceRequest
	 */
	private void convertLongTimeToDate(UserEventsRequest traceRequest) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(traceRequest!=null && traceRequest.getDeviceTime()!=null) {
			String utcDate = sdf.format(new Date(Long.valueOf(traceRequest.getDeviceTime())));
			traceRequest.setDeviceTime(utcDate);
		}
	}

}