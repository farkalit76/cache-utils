package ae.gov.sdg.paperless.platform.cache.service;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import ae.gov.sdg.paperless.platform.exceptions.DNRuntimeException;

/**
 * @author c_chandra.bommise
 *
 * @param <T>
 *
 *            Cache consumer functionalities.
 */
@SuppressWarnings("unchecked")
public interface IRedisCacheService<T> {

	default String generateCacheName(final String journeyType, final String... parameter) {
		if (isBlank(journeyType) && parameter == null) {
			throw new DNRuntimeException("Cache Name cannot be empty");
		} else {
			String cacheSuffix = "";
			if (parameter != null) {
				cacheSuffix = join(parameter, "-");
			}
			return journeyType.concat("-").concat(cacheSuffix);
		}
	}

	default String generateGUIDCacheName(final String journeyType) {
		if (isBlank(journeyType)) {
			throw new DNRuntimeException("Journey Type cannot be empty");
		} else {
			return journeyType.concat("-").concat(randomUUID().toString());
		}
	}

	// POST /add key,value, ttl in body
	boolean add(String key, T value, Long timeInMinutes) throws IOException;

	// GET queryparam
	T get(String key) throws IOException;

	boolean exists(String key) throws IOException;

	Map<String, String> metadata(String key) throws IOException;

	String[] dbKeys(String pattern) throws IOException;

	// POST /addKeyValue keySpace,key,value in body
	boolean keyValuePut(String keySpace, String key, T value, Long timeInMinutes)
			throws IOException;

	// POST /addKeyValue keySpace,key,value in body
	boolean keyValuePut(String keySpace, Map<String, Object> value, Long timeInMinutes)
			throws IOException;

	// GET /getkeyvalue
	T keyValueGet(String keySpace, String key) throws IOException;

	// GET /keys
	Set<String> keys(String keySpace) throws IOException;

	// GET /entries
	Map<String, Object> keyValueEntries(String keySpace) throws IOException;

	// DELETE
	Boolean delete(String keySpace) throws IOException;

	// DELETE /key
	Long delete(String keySpace, String key) throws IOException;

	boolean pushInQueue(String key, Long timeInMinutes, T... value)
			throws IOException;

	T popFromEnd(String key) throws IOException;

	T popFromStart(String key) throws IOException;

	T fetchFromIndex(String key, Integer index) throws IOException;

	T[] fetchAllFromQueue(String key) throws IOException;

	Long removeFromQueue(String key, T value, Integer number)
			throws IOException;

	// Set operations
	Long addToSet(String key, Long timeInMinutes, T... value)
			throws IOException;

	Long removeFromSet(String key, T... value) throws IOException;

	T popFromSet(String key) throws IOException;

	T[] fetchAllFromSet(String key) throws IOException;

	/***
	 * Set the header parameters
	 * 
	 * @return
	 */
	default HttpHeaders setHttpHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}