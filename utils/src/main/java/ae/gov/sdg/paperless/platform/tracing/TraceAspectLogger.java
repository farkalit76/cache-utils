package ae.gov.sdg.paperless.platform.tracing;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ae.gov.sdg.paperless.platform.common.PlatformConfig;
 
/**
 * @author c_chandra.bommise
 * 
 * Aspect for logging.
 *
 */
@Aspect
@Component
public class TraceAspectLogger {
    private static final Logger logger = LoggerFactory.getLogger(TraceAspectLogger.class);
    
    @Autowired
    private PlatformConfig platformConfig;
 
    /**
     * Log the request and response based on annotation TraceLog on method.
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* ae.gov.sdg.paperless..*(..)) && @annotation(ae.gov.sdg.paperless.platform.tracing.TraceLog)")
    public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable{
    	long start = System.currentTimeMillis();
        Object response = null;
        final String method = joinPoint.getSignature().toShortString();
        final Object[] args = joinPoint.getArgs();
        try {
        	final StringBuilder builder = new StringBuilder();
        	builder.append("Starting - ").append(method);
        	String parameterStr;
        	if(platformConfig.isLogLevelVerbose() && isValid(args)) {
            	builder.append(" with parameters: ");
                for (final Object obj : args) {
                	builder.append(obj);
                }
                parameterStr = builder.toString();
            	if(parameterStr!=null && parameterStr.length()>1000) {
            		parameterStr = parameterStr.substring(0, 999);
                }
        	} else {
        		parameterStr = builder.toString();
        	}
        	
            final LogMessage messageBuilder = new LogMessageBuilder().setRequestBody(parameterStr).build();
            logger.info("LogMessage - {}",messageBuilder);
            response = joinPoint.proceed();
            logger.info("Inbound Process - {}, execution time {} ms", method, System.currentTimeMillis() - start);
            return response;
        } catch (final Exception e) {
        	logger.error("Inbound Process - {}, execution time {} ms", method, System.currentTimeMillis() - start);
            throw e;
        }
    }

	/**
	 * @param args
	 * @return
	 */
	private boolean isValid(final Object[] args) {
		return args != null && args.length > 0;
	}
}