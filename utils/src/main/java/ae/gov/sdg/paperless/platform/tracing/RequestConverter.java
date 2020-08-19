package ae.gov.sdg.paperless.platform.tracing;

import java.util.UUID;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author c_chandra.bommise
 * 
 * Converter for log pattern. 
 */
public class RequestConverter extends ClassicConverter {
	
    @Override
    public String convert(final ILoggingEvent event) {
        return UUID.randomUUID().toString();
    }

}