package ae.gov.sdg.paperless.platform.cache.service.json;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.PARAMETERS;
import static ae.gov.sdg.paperless.platform.util.JsonUtil.fromJson;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ae.gov.sdg.paperless.platform.cache.model.BaseResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheHashKeysResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonHashCollectionRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonHashCollectionResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonHashRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonListRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonListResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringListResponse;
import ae.gov.sdg.paperless.platform.cache.service.IRedisCacheService;
import ae.gov.sdg.paperless.platform.common.PlatformConfig;
import ae.gov.sdg.paperless.platform.common.model.BasicAuth;
import ae.gov.sdg.paperless.platform.common.service.generic.RestBasicAuthService;
import ae.gov.sdg.paperless.platform.exceptions.ExternalApiException;


/**
 * @author c_chandra.bommise
 *
 * @deprecated 
 * This class will be deprecated. Replaced by {@link #RedisSimpleJsonCacheService}
 * Cache consumer for json
 *
 */
@Service
@Deprecated
public class RedisJsonCacheService implements IRedisCacheService<JSONObject>{
	
	/**
	 * 
	 */
	private static final String NUMBER_OF_RECORDS = "numberOfRecords";

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisJsonCacheService.class);
	
	public static final Long TTL = 480L;

	private final RestBasicAuthService restTemplate;

	private final PlatformConfig config;

	private final ObjectMapper objectMapper;

	private String baseCacheUrl;
	
	private BasicAuth basicAuth;
	
	/**
	 * Constructor injection
	 * 
	 * @param restTemplate
	 * @param config
	 * @param objectMapper
	 */
	public RedisJsonCacheService(final @Qualifier("commonServicesRestTemplate") RestBasicAuthService restTemplate, final PlatformConfig config, final ObjectMapper objectMapper) {
		super();
		this.restTemplate = restTemplate;
		this.config = config;
		this.objectMapper = objectMapper;
	}

	/**
	 * Post constructor for initializing URL.
	 */
	@PostConstruct
	public void init() {
		baseCacheUrl = config.getDubaiNowCacheContextUrl().concat("api/cachestore");
		basicAuth = new BasicAuth(config.getDubaiNowCacheUserName(), config.getDubaiNowCachePassword());
	}

	/**
	 * Check if key exists in the cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public boolean exists(final String key) throws IOException {
		LOGGER.info("Start cache exists for key:{} ",key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final CacheJsonResponse dataResponse = restTemplate.invoke(
				baseCacheUrl
				.concat("/exists?key=".concat(key)),
				HttpMethod.GET, entity, CacheJsonResponse.class, basicAuth).getBody();
		if (dataResponse.isSuccess() && dataResponse.getData()!=null) {
				final JSONObject obj = new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
				if(obj.has("exists")) {
					LOGGER.info("End cache exists for key:{} found",key);
					return (boolean)obj.get("exists");
				}
		}
		LOGGER.info("End cache exists for key:{} not found",key);
		return false;
	}


	/**
	 * Fetches the meta data like ttl etc.. based on the key.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> metadata(final String key) throws IOException {
		LOGGER.info("Start metadata for key:{} ",key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final CacheJsonResponse dataResponse = restTemplate.invoke(
				baseCacheUrl
				.concat("/plaintext/metadata?key=".concat(key)),
				HttpMethod.GET, entity, CacheJsonResponse.class, basicAuth).getBody();
		if ( dataResponse.isSuccess() && dataResponse.getData()!=null) {
				final JSONObject obj = new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
				if(obj.has("metadata")) {
					LOGGER.info("End metadata for key:{} ",key);
					return fromJson((JSONObject)obj.get("metadata"), HashMap.class);
				}
		}
		LOGGER.info("End metadata for key:{} doesn't exist",key);
		return null;
	}

	/**
	 * Fetch all the keys available in cache for the pattern provided.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public String[] dbKeys(final String pattern) throws IOException {
		LOGGER.info("Start dbKeys for pattern:{} ",pattern);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("/plaintext/dbKeys?pattern=".concat(pattern)),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheStringListResponse dataResponse = fromJson(response, CacheStringListResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End dbKeys for pattern:{} found",pattern);
			return dataResponse.getData();
		}
		LOGGER.info("End dbKeys for pattern:{} not found",pattern);
		return new String[0];
	}

	/**
	 * Add the key value in the cache with the ttl provided is not null else with default ttl.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public boolean add(final String key, final JSONObject value, final Long timeInMinutes) throws IOException {
		LOGGER.info("Start add for key:{} timeInMinutes:{}",key,timeInMinutes);
		final CacheJsonRequest cacheRequest = new CacheJsonRequest();
		cacheRequest.setKey(key);
		final org.json.simple.JSONObject json = jsonConversion(key, timeInMinutes, value);
		if(json==null) {
			return false;
		}
		cacheRequest.setValue(json);
		
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheJsonRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/add"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = fromJson(response, BaseResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End add for key:{} timeInMinutes:{} successful",key,timeInMinutes);
			return true;
		}
		LOGGER.info("End add for key:{} timeInMinutes:{} un-successful",key,timeInMinutes);
		return false;

	}

	/**
	 * Fetch the value based on the key from cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject get(final String key) throws IOException {
		LOGGER.info("Start get for key:{}",key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("?key=".concat(key)),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End get for key:{} found",key);
			return new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
		}
		LOGGER.info("End get for key:{} not found",key);
		return null;
	}

	/**
	 * Add the key value list under the keyspace in cache with the ttl provided is not null else with default ttl.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public boolean keyValuePut(final String keySpace, final String key, final JSONObject value, final Long timeInMinutes) throws IOException {
		LOGGER.info("Start keyValuePut for keySpace:{} key:{} timeInMinutes:{}",keySpace, key, timeInMinutes);
		final CacheJsonHashRequest cacheRequest = new CacheJsonHashRequest();
		cacheRequest.setKeySpace(keySpace);
		cacheRequest.setKey(key);
		final org.json.simple.JSONObject json = jsonConversion(key, timeInMinutes, value);
		if(json==null) {
			return false;
		}
		cacheRequest.setValue(json);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheJsonHashRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/addkeyvalue"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = fromJson(response, BaseResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End keyValuePut for keySpace: {} key:{} timeInMinutes:{} successful",keySpace, key, timeInMinutes);
			return true;
		}
		LOGGER.info("End keyValuePut for keySpace:{} key:{} timeInMinutes:{} un-successful",keySpace, key, timeInMinutes);
		return false;

	}

	/**
	 * Add the key value list under the keyspace in cache with the ttl provided is not null else with default ttl.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public boolean keyValuePut(final String keySpace, final Map<String, Object> value, final Long timeInMinutes) throws IOException {
		LOGGER.info("Start keyValuePut for keySpace:{} timeInMinutes:{}",keySpace, timeInMinutes);
		final CacheJsonHashCollectionRequest cacheRequest = new CacheJsonHashCollectionRequest();
		cacheRequest.setKeySpace(keySpace);
		cacheRequest.setCacheMap(value);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheJsonHashCollectionRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/addkeyvalues"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = fromJson(response, BaseResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End keyValuePut for keySpace:{} timeInMinutes:{} successful",keySpace, timeInMinutes);
			return true;
		}
		LOGGER.info("End keyValuePut for keySpace:{} timeInMinutes:{} un-successful",keySpace, timeInMinutes);
		return false;

	}

	/**
	 * Fetch the value based on the keyspace and key from cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject keyValueGet(final String cacheName, final String key) throws IOException {
		LOGGER.info("Start keyValueGet for cacheName:{} key:{}",cacheName, key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("/getkeyvalue?keySpace=" + cacheName + "&key=".concat(key)),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End keyValueGet for cacheName:{} key:{} found",cacheName, key);
		    return new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
		}
		LOGGER.info("End keyValueGet for cacheName:{} key:{} not found",cacheName, key);
		return null;
	}

	/**
	 * Fetch the keys available under the keyspace from cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Set<String> keys(final String cacheName) throws IOException {
		LOGGER.info("Start keys for cacheName:{}",cacheName);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("/keys?keySpace=" + cacheName),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheHashKeysResponse dataResponse = fromJson(response, CacheHashKeysResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End keys for cacheName:{} found",cacheName);
			return dataResponse.getData();
		}
		LOGGER.info("End keys for cacheName:{} not found",cacheName);
		return emptySet();
	}

	/**
	 * Fetch the key value entries available under the keyspace from cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Map<String, Object> keyValueEntries(final String cacheName) throws IOException {
		LOGGER.info("Start keyValueEntries for cacheName:{}",cacheName);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("/entries?keySpace=" + cacheName),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheJsonHashCollectionResponse dataResponse = fromJson(response, CacheJsonHashCollectionResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End keyValueEntries for cacheName:{} found",cacheName);
			return dataResponse.getData();
		}
		LOGGER.info("End keyValueEntries for cacheName:{} not found",cacheName);
		return null;
	}

	/**
	 * Delete the keyspace/key from cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Boolean delete(final String keySpace) throws IOException {
		LOGGER.info("Start delete for keySpace:{}",keySpace);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"?keySpace=".concat(keySpace)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = fromJson(response, BaseResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End delete for keySpace:{} successful",keySpace);
			return true;
		}
		LOGGER.info("End delete for keySpace:{} un-successful",keySpace);
		return false;
	}

	/**
	 * Delete the key under the keySpace from cache
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Long delete(final String keySpace, final String key) throws IOException {
		LOGGER.info("Start delete for keySpace:{} key:{}",keySpace, key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/key?keySpace=".concat(keySpace).concat("&key=").concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null ) {
				final JSONObject obj = new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
				if(obj.has(NUMBER_OF_RECORDS)) {
					LOGGER.info("End delete for keySpace:{} key:{} successful",keySpace, key);
					return Long.valueOf((Integer)((LinkedHashMap) dataResponse.getData()).get(NUMBER_OF_RECORDS));
				}
		}
		LOGGER.info("End delete for keySpace:{} key:{} un-successful",keySpace, key);
		return null;
	}

	/**
	 * Add the key and values in the queue with the ttl provided is not null else with default ttl.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public boolean pushInQueue(final String key, final Long timeInMinutes,final JSONObject... value) throws IOException {
		LOGGER.info("Start pushInQueue for key:{} timeInMinutes:{}",key, timeInMinutes);
		final CacheJsonListRequest cacheRequest = new CacheJsonListRequest();
		cacheRequest.setKey(key);
		final org.json.simple.JSONObject[] json = jsonConversion(key, timeInMinutes, value);
		if(json.length==0) {
			return false;
		}
		cacheRequest.setValue(json);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheJsonListRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/queue/push"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = fromJson(response, BaseResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End pushInQueue for key:{} timeInMinutes:{} successful",key, timeInMinutes);
			return true;
		}
		LOGGER.info("End pushInQueue for key:{} timeInMinutes:{} un-successful",key, timeInMinutes);
		return false;

	}

	/**
	 * Pop the value from end of the queue for key
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject popFromEnd(final String key) throws IOException {
		LOGGER.info("Start popFromEnd for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/queue/popfromend?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End popFromEnd for key:{} successful",key);
			return new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
		}
		LOGGER.info("End popFromEnd for key:{} un-successful",key);
		return null;
	}

	/**
	 * Pop the value from start of the queue for key
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject popFromStart(final String key) throws IOException {
		LOGGER.info("Start popFromStart for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/queue/popfromstart?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End popFromStart for key:{} successful",key);
			return new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
		}
		LOGGER.info("End popFromStart for key:{} un-successful",key);
		return null;
	}

	/**
	 * Fetch the values from the index for given key from the queue 
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject fetchFromIndex(final String key, final Integer index) throws IOException {
		LOGGER.info("Start fetchFromIndex for key:{} index:{}",key, index);
		final RequestEntity<Void> requestEntity = RequestEntity
				.get(URI.create(baseCacheUrl+"/queue/fetchfromindex?key=".concat(key).concat("&index=")+index))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End fetchFromIndex for key:{} index:{} successful",key, index);
			return new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
		}
		LOGGER.info("End fetchFromIndex for key:{} index:{} un-successful",key, index);
		return null;
	}

	/**
	 * Fetch all the values for given key from the queue
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject[] fetchAllFromQueue(final String key) throws IOException {
		LOGGER.info("Start fetchAllFromQueue for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.get(URI.create(baseCacheUrl+"/queue/fetchall?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonListResponse dataResponse = fromJson(response, CacheJsonListResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End fetchAllFromQueue for key:{} successful",key);
			return dataResponse.getData();
		}
		LOGGER.info("End fetchAllFromQueue for key:{} un-successful",key);
		return new JSONObject[0];
	}

	/**
	 * Remove the number of values for given key from the queue
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Long removeFromQueue(final String key, final JSONObject value, final Integer numberOfValues) throws IOException {
		LOGGER.info("Start removeFromQueue for key:{} value:{} numberOfValues:{}",key, value, numberOfValues);
		final RequestEntity<JSONObject> requestEntity = ((BodyBuilder) RequestEntity
				.delete(URI.create(baseCacheUrl+"/queue?key=".concat(key).concat("&numberOfValues=")+numberOfValues))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))).body(value);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null) {
				final JSONObject obj = new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
				if(obj.has(NUMBER_OF_RECORDS)) {
					LOGGER.info("End removeFromQueue for key:{} value:{} numberOfValues:{} successful",key, value, numberOfValues);
					return Long.valueOf((Integer)((LinkedHashMap) dataResponse.getData()).get(NUMBER_OF_RECORDS));
				}
		}
		LOGGER.info("End removeFromQueue for key:{} value:{} numberOfValues:{} un-successful",key, value, numberOfValues);
		return null;
	}

	/**
	 * Add the key and values to the set with the ttl provided is not null else with default ttl.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Long addToSet(final String key, final Long timeInMinutes, final JSONObject... value) throws IOException {
		LOGGER.info("Start addToSet for key:{} timeInMinutes:{}",key, timeInMinutes);
		final CacheJsonListRequest cacheRequest = new CacheJsonListRequest();
		cacheRequest.setKey(key);
		final org.json.simple.JSONObject[] json = jsonConversion(key, timeInMinutes, value);
		if(json.length==0) {
			return null;
		}
		cacheRequest.setValue(json);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheJsonListRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/set"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null) {
				final JSONObject obj = new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
				if(obj.has(NUMBER_OF_RECORDS)) {
					LOGGER.info("End addToSet for key:{} timeInMinutes:{} successful",key, timeInMinutes);
					return Long.valueOf((Integer)((LinkedHashMap) dataResponse.getData()).get(NUMBER_OF_RECORDS));
				}
		}
		LOGGER.info("End addToSet for key:{} timeInMinutes:{} un-successful",key, timeInMinutes);
		return null;
	}


	/**
	 * Remove the number of values for given key from the set
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Long removeFromSet(final String key, final JSONObject... value) throws IOException {
		LOGGER.info("Start removeFromSet for key:{}",key);
		final CacheJsonListRequest cacheRequest = new CacheJsonListRequest();
		cacheRequest.setKey(key);
		final org.json.simple.JSONObject[] json = jsonConversion(key, value);
		if(json.length==0) {
			return null;
		}
		cacheRequest.setValue(json);
		final RequestEntity<CacheJsonListRequest> requestEntity = ((BodyBuilder) RequestEntity
				.delete(URI.create(baseCacheUrl+"/set"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null ) {
				final JSONObject obj = new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
				if(obj.has(NUMBER_OF_RECORDS)) {
					LOGGER.info("End removeFromSet for key:{} successful",key);
					return Long.valueOf((Integer)((LinkedHashMap) dataResponse.getData()).get(NUMBER_OF_RECORDS));
				}
		}
		LOGGER.info("End removeFromSet for key:{} un-successful",key);
		return null;
	}

	/**
	 * Remove the data for given key from the set
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject popFromSet(final String key) throws IOException {
		LOGGER.info("Start popFromSet for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/set/pop?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonResponse dataResponse = fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End popFromSet for key:{} successful",key);
			return new JSONObject(objectMapper.writeValueAsString(dataResponse.getData()));
		}
		LOGGER.info("End popFromSet for key:{} un-successful",key);
		return null;
	}

	/**
	 * Fetch all values for given key from the set
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public JSONObject[] fetchAllFromSet(final String key) throws IOException {
		LOGGER.info("Start fetchAllFromSet for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.get(URI.create(baseCacheUrl+"/set?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheJsonListResponse dataResponse = fromJson(response, CacheJsonListResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End fetchAllFromSet for key:{} successful",key);
			return dataResponse.getData();
		}
		LOGGER.info("End fetchAllFromSet for key:{} un-successful",key);
		return new JSONObject[0];
	}
	
	private org.json.simple.JSONObject jsonConversion(final String key, final Long timeInMinutes, final JSONObject value) {
		final JSONParser parser = new JSONParser();
		try {
			return (org.json.simple.JSONObject) parser.parse(value.toString());
		} catch (final ParseException e) {
			LOGGER.info("End for key:{} timeInMinutes:{} un-successful",key,timeInMinutes);
		}
		return null;
	}
	
	private org.json.simple.JSONObject[] jsonConversion(final String key, final Long timeInMinutes, final JSONObject... value) {
		final JSONParser parser = new JSONParser();
		try {
			final List<org.json.simple.JSONObject> array = new ArrayList<>();
			for(final JSONObject val : value) {
				array.add((org.json.simple.JSONObject) parser.parse(val.toString()));
			}
 			return array.toArray(new org.json.simple.JSONObject[0]);
		} catch (final ParseException e) {
			LOGGER.info("End for key:{} timeInMinutes:{} un-successful",key,timeInMinutes);
		}
		return emptyList().toArray(new org.json.simple.JSONObject[0]);
	}
	
	private org.json.simple.JSONObject[] jsonConversion(final String key, final JSONObject... value) {
		final JSONParser parser = new JSONParser();
		try {
			final List<org.json.simple.JSONObject> array = new ArrayList<>();
			for(final JSONObject val : value) {
				array.add((org.json.simple.JSONObject) parser.parse(val.toString()));
			}
 			return array.toArray(new org.json.simple.JSONObject[0]);
		} catch (final ParseException e) {
			LOGGER.info("End for key:{} un-successful",key);
		}
		return emptyList().toArray(new org.json.simple.JSONObject[0]);
	}

}