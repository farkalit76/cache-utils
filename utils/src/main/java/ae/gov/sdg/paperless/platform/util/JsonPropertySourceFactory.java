package ae.gov.sdg.paperless.platform.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author c_chandra.bommise
 * 
 * Loading the json property sources
 *
 */
public class JsonPropertySourceFactory implements PropertySourceFactory {
	
	private static final ObjectMapper objectMapper;

    static {
        final ObjectMapper mapper = new ObjectMapper();
        objectMapper = mapper;
    }

	/**
	 * Read the resources and load the json properties as map.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PropertySource<?> createPropertySource(final String name, final EncodedResource resource)
			throws IOException {
		final Map readValue = objectMapper.readValue(resource.getInputStream(), Map.class);
		return new MapPropertySource("json-property", readValue);
	}
}