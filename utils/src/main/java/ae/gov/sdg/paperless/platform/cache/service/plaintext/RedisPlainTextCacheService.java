package ae.gov.sdg.paperless.platform.cache.service.plaintext;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.PARAMETERS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.stereotype.Service;

import ae.gov.sdg.paperless.platform.cache.model.BaseResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheHashKeysResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheJsonResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringHashCollectionRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringHashCollectionResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringHashRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringListRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringListResponse;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringRequest;
import ae.gov.sdg.paperless.platform.cache.model.CacheStringResponse;
import ae.gov.sdg.paperless.platform.cache.service.IRedisCacheService;
import ae.gov.sdg.paperless.platform.common.PlatformConfig;
import ae.gov.sdg.paperless.platform.common.model.BasicAuth;
import ae.gov.sdg.paperless.platform.common.service.generic.RestBasicAuthService;
import ae.gov.sdg.paperless.platform.exceptions.ExternalApiException;
import ae.gov.sdg.paperless.platform.util.JsonUtil;

/**
 * @author c_chandra.bommise
 *
 * Cache consumer for plain text
 *
 */
@Service
public class RedisPlainTextCacheService implements IRedisCacheService<String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisPlainTextCacheService.class);

    public static final Long TTL = 480L;

    private final RestBasicAuthService restTemplate;

    private final PlatformConfig config;

    private String baseCacheUrl;
    
    private BasicAuth basicAuth;
    
    /**
     * Constructor injection
     * 
     * @param restTemplate
     * @param config
     */
    public RedisPlainTextCacheService(final @Qualifier("commonServicesRestTemplate") RestBasicAuthService restTemplate, final PlatformConfig config) {
		super();
		this.restTemplate = restTemplate;
		this.config = config;
	}

    /**
	 * Post constructor for initializing URL.
	 */
    @PostConstruct
    public void init() {
    	baseCacheUrl = config.getDubaiNowCacheContextUrl().concat("api/cachestore/plaintext");
    	basicAuth = new BasicAuth(config.getDubaiNowCacheUserName(), config.getDubaiNowCachePassword());
    }

    /**
     * Check if key exists in the cache
     */
    @Override
   	public boolean exists(final String key) throws IOException {
    	LOGGER.info("Start cache exists for key:{} ",key);
       	final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
           final String response = restTemplate.invoke(
           		baseCacheUrl
                           .concat("/exists?key=".concat(key)),
                   HttpMethod.GET, entity, String.class, basicAuth).getBody();
           final CacheJsonResponse dataResponse = JsonUtil.fromJson(response, CacheJsonResponse.class);
           if (dataResponse != null && dataResponse.isSuccess() && isDataExists(dataResponse, "exists")) {
	       		LOGGER.info("End cache exists for key:{} found",key);
	       		return (boolean)((LinkedHashMap) dataResponse.getData()).get("exists");
           }
           LOGGER.info("End cache exists for key:{} not found",key);
           return false;
   	}

	/**
	 * @param dataResponse
	 * @return
	 */
	private boolean isDataExists(final CacheJsonResponse dataResponse, String dataType) {
		return dataResponse.getData()!=null && ((LinkedHashMap) dataResponse.getData()).containsKey(dataType);
	}

    /**
     * Fetches the meta data like ttl etc.. based on the key.
     */
    @SuppressWarnings("unchecked")
	@Override
	public Map<String, String> metadata(final String key) throws IOException {
    	LOGGER.info("Start metadata for key:{} ",key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("/metadata?key=".concat(key)),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheJsonResponse dataResponse = JsonUtil.fromJson(response, CacheJsonResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && isDataExists(dataResponse, "metadata")) {
			LOGGER.info("End metadata for key:{} ",key);
			return (Map<String, String>)((LinkedHashMap) dataResponse.getData()).get("metadata");
		}
		LOGGER.info("End metadata for key:{} doesn't exist",key);
		return null;
	}


	/**
	 * Fetch all the keys available in cache for the pattern provided.
	 */
	@Override
	public String[] dbKeys(final String pattern) throws IOException {
		LOGGER.info("Start dbKeys for pattern:{} ",pattern);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("/dbKeys?pattern=".concat(pattern)),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheStringListResponse dataResponse = JsonUtil.fromJson(response, CacheStringListResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End dbKeys for pattern:{} found",pattern);
			return dataResponse.getData();
		}
		LOGGER.info("End dbKeys for pattern:{} not found",pattern);
		return new String[0];
	}

	/**
	 * Add the key value in the cache with the ttl provided is not null else with default ttl.
	 */
	@Override
	public boolean add(final String key, final String value, final Long timeInMinutes) throws IOException {
		LOGGER.info("Start add for key:{} timeInMinutes:{}",key,timeInMinutes);
		final CacheStringRequest cacheRequest = new CacheStringRequest();
		cacheRequest.setKey(key);
		cacheRequest.setValue(value);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheStringRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/add"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = JsonUtil.fromJson(response, BaseResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End add for key:{} timeInMinutes:{} successful",key,timeInMinutes);
			return true;
		}
		LOGGER.info("End add for key:{} timeInMinutes:{} un-successful",key,timeInMinutes);
		return false;
	}

	/**
	 * Fetch the value based on the key from cache
	 */
	@Override
	public String get(final String key) throws IOException {
		LOGGER.info("Start get for key:{}",key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
		final String response = restTemplate.invoke(
				baseCacheUrl
				.concat("?key=".concat(key)),
				HttpMethod.GET, entity, String.class, basicAuth).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End get for key:{} found",key);
			return dataResponse.getData();
		}
		LOGGER.info("End get for key:{} not found",key);
		return null;
	}

	/**
	 * Add the key value under the keyspace in cache with the ttl provided is not null else with default ttl.
	 */
	@Override
	public boolean keyValuePut(final String keySpace, final String key, final String value, final Long timeInMinutes) throws IOException {
		LOGGER.info("Start keyValuePut for keySpace:{} key:{} timeInMinutes:{}",keySpace, key, timeInMinutes);
		final CacheStringHashRequest cacheRequest = new CacheStringHashRequest();
		cacheRequest.setKeySpace(keySpace);
		cacheRequest.setKey(key);
		cacheRequest.setValue(value);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheStringHashRequest> requestEntity = RequestEntity
                .post(URI.create(baseCacheUrl+"/addkeyvalue"))
                .accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
                .body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = JsonUtil.fromJson(response, BaseResponse.class);
        if (dataResponse != null && dataResponse.isSuccess()) {
        	LOGGER.info("End keyValuePut for keySpace: {} key:{} timeInMinutes:{} successful",keySpace, key, timeInMinutes);
            return true;
        }
        LOGGER.info("End keyValuePut for keySpace:{} key:{} timeInMinutes:{} un-successful",keySpace, key, timeInMinutes);
        return false;

	}

	/**
	 * Add the key value list under the keyspace in cache with the ttl provided is not null else with default ttl.
	 */
	@Override
	public boolean keyValuePut(final String keySpace, final Map<String, Object> value, final Long timeInMinutes) throws IOException {
		LOGGER.info("Start keyValuePut for keySpace:{} timeInMinutes:{}",keySpace, timeInMinutes);
		final CacheStringHashCollectionRequest cacheRequest = new CacheStringHashCollectionRequest();
		cacheRequest.setKeySpace(keySpace);
		cacheRequest.setCacheMap(value);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheStringHashCollectionRequest> requestEntity = RequestEntity
                .post(URI.create(baseCacheUrl+"/addkeyvalues"))
                .accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
                .body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = JsonUtil.fromJson(response, BaseResponse.class);
        if (dataResponse != null && dataResponse.isSuccess()) {
        	LOGGER.info("End keyValuePut for keySpace:{} timeInMinutes:{} successful",keySpace, timeInMinutes);
            return true;
        }
        LOGGER.info("End keyValuePut for keySpace:{} timeInMinutes:{} un-successful",keySpace, timeInMinutes);
        return false;

	}

	/**
	 * Fetch the value based on the keyspace and key from cache
	 */
	@Override
	public String keyValueGet(final String cacheName, final String key) throws IOException {
		LOGGER.info("Start keyValueGet for cacheName:{} key:{}",cacheName, key);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
        final String response = restTemplate.invoke(
        		baseCacheUrl
                        .concat("/getkeyvalue?keySpace=" + cacheName + "&key=".concat(key)),
                HttpMethod.GET, entity, String.class, basicAuth).getBody();
        final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
        if (dataResponse != null && dataResponse.isSuccess()) {
        	LOGGER.info("End keyValueGet for cacheName:{} key:{} found",cacheName, key);
            return dataResponse.getData();
        }
        LOGGER.info("End keyValueGet for cacheName:{} key:{} not found",cacheName, key);
        return null;
	}

	/**
	 * Fetch the keys available under the keyspace from cache
	 */
	@Override
	public Set<String> keys(final String cacheName) throws IOException {
		LOGGER.info("Start keys for cacheName:{}",cacheName);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
        final String response = restTemplate.invoke(
        		baseCacheUrl
                        .concat("/keys?keySpace=" + cacheName),
                HttpMethod.GET, entity, String.class, basicAuth).getBody();
        final CacheHashKeysResponse dataResponse = JsonUtil.fromJson(response, CacheHashKeysResponse.class);
        if (dataResponse != null && dataResponse.isSuccess()) {
        	LOGGER.info("End keys for cacheName:{} found",cacheName);
            return dataResponse.getData();
        }
        LOGGER.info("End keys for cacheName:{} not found",cacheName);
        return Collections.emptySet();
	}

	/**
	 * Fetch the key value entries available under the keyspace from cache
	 */
	@Override
	public Map<String, Object> keyValueEntries(final String cacheName) throws IOException {
		LOGGER.info("Start keyValueEntries for cacheName:{}",cacheName);
		final HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, setHttpHeaders());
        final String response = restTemplate.invoke(
        		baseCacheUrl
                        .concat("/entries?keySpace=" + cacheName),
                HttpMethod.GET, entity, String.class, basicAuth).getBody();
        final CacheStringHashCollectionResponse dataResponse = JsonUtil.fromJson(response, CacheStringHashCollectionResponse.class);
        if (dataResponse != null && dataResponse.isSuccess()) {
        	LOGGER.info("End keyValueEntries for cacheName:{} found",cacheName);
            return dataResponse.getData();
        }
        LOGGER.info("End keyValueEntries for cacheName:{} not found",cacheName);
        return null;
	}

	/**
	 * Delete the keyspace/key from cache
	 */
	@Override
	public Boolean delete(final String keySpace) throws IOException {
		LOGGER.info("Start delete for keySpace:{}",keySpace);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"?keySpace=".concat(keySpace)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = JsonUtil.fromJson(response, BaseResponse.class);
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
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null ) {
				LOGGER.info("End delete for keySpace:{} key:{} successful",keySpace, key);
				return Long.valueOf(dataResponse.getData());
		}
		LOGGER.info("End delete for keySpace:{} key:{} un-successful",keySpace, key);
		return null;
	}

	/**
	 * Add the key and values to the queue with the ttl provided is not null else with default ttl.
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public boolean pushInQueue(final String key, final Long timeInMinutes, final String... value) throws IOException {
		LOGGER.info("Start pushInQueue for key:{} timeInMinutes:{}",key, timeInMinutes);
		final CacheStringListRequest cacheRequest = new CacheStringListRequest();
		cacheRequest.setKey(key);
		cacheRequest.setValue(value);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheStringListRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/queue/push"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final BaseResponse dataResponse = JsonUtil.fromJson(response, BaseResponse.class);
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
	public String popFromEnd(final String key) throws IOException {
		LOGGER.info("Start popFromEnd for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/queue/popfromend?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End popFromEnd for key:{} successful",key);
			return dataResponse.getData();
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
	public String popFromStart(final String key) throws IOException {
		LOGGER.info("Start popFromStart for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/queue/popfromstart?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End popFromStart for key:{} successful",key);
			return dataResponse.getData();
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
	public String fetchFromIndex(final String key, final Integer index) throws IOException {
		LOGGER.info("Start fetchFromIndex for key:{} index:{}",key, index);
		final RequestEntity<Void> requestEntity = RequestEntity
				.get(URI.create(baseCacheUrl+"/queue/fetchfromindex?key=".concat(key).concat("&index=")+index))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End fetchFromIndex for key:{} index:{} successful",key, index);
			return dataResponse.getData();
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
	public String[] fetchAllFromQueue(final String key) throws IOException {
		LOGGER.info("Start fetchAllFromQueue for key:{}",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.get(URI.create(baseCacheUrl+"/queue/fetchall?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringListResponse dataResponse = JsonUtil.fromJson(response, CacheStringListResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End fetchAllFromQueue for key:{} successful",key);
			return dataResponse.getData();
		}
		LOGGER.info("End fetchAllFromQueue for key:{} un-successful",key);
		return new String[0];
	}

	/**
	 * Remove the number of values for given key from the queue
	 * @throws URISyntaxException 
	 * @throws ExternalApiException 
	 */
	@Override
	public Long removeFromQueue(final String key, final String value, final Integer numberOfValues) throws IOException {
		LOGGER.info("Start removeFromQueue for key:{} value:{} numberOfValues:{}",key, value, numberOfValues);
		final RequestEntity<String> requestEntity = ((BodyBuilder) RequestEntity
				.delete(URI.create(baseCacheUrl+"/queue?key=".concat(key).concat("&numberOfValues=")+numberOfValues))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))).body(value);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null ) {
			LOGGER.info("End removeFromQueue for key:{} value:{} numberOfValues:{} successful",key, value, numberOfValues);
			return Long.valueOf(dataResponse.getData());
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
	public Long addToSet(final String key, final Long timeInMinutes, final String... value) throws IOException {
		LOGGER.info("Start addToSet for key:{} timeInMinutes:{}",key, timeInMinutes);
		final CacheStringListRequest cacheRequest = new CacheStringListRequest();
		cacheRequest.setKey(key);
		cacheRequest.setValue(value);
		cacheRequest.setTimeInMinutes(timeInMinutes != null ? timeInMinutes : TTL);
		final RequestEntity<CacheStringListRequest> requestEntity = RequestEntity
				.post(URI.create(baseCacheUrl+"/set"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null ) {
			LOGGER.info("End addToSet for key:{} timeInMinutes:{} successful",key, timeInMinutes);
			return Long.valueOf(dataResponse.getData());
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
	public Long removeFromSet(final String key, final String... value) throws IOException {
		LOGGER.info("Start removeFromSet for key:{}",key);
		final CacheStringListRequest cacheRequest = new CacheStringListRequest();
		cacheRequest.setKey(key);
		cacheRequest.setValue(value);
		final RequestEntity<CacheStringListRequest> requestEntity = ((BodyBuilder) RequestEntity
				.delete(URI.create(baseCacheUrl+"/set"))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)))
				.body(cacheRequest);
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess() && dataResponse.getData()!=null ) {
			LOGGER.info("End removeFromSet for key:{} successful",key);
			return Long.valueOf(dataResponse.getData());
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
	public String popFromSet(final String key) throws IOException {
		LOGGER.info("Start popFromSet for key:{} un-successful",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.delete(URI.create(baseCacheUrl+"/set/pop?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringResponse dataResponse = JsonUtil.fromJson(response, CacheStringResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End popFromSet for key:{} successful",key);
			return dataResponse.getData();
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
	public String[] fetchAllFromSet(final String key) throws IOException {
		LOGGER.info("Start fetchAllFromSet for key:{} un-successful",key);
		final RequestEntity<Void> requestEntity = RequestEntity
				.get(URI.create(baseCacheUrl+"/set?key=".concat(key)))
				.accept(MediaType.ALL).headers(RestBasicAuthService.authHeaders(basicAuth)).build();
		final String response = restTemplate.invoke(requestEntity, String.class).getBody();
		final CacheStringListResponse dataResponse = JsonUtil.fromJson(response, CacheStringListResponse.class);
		if (dataResponse != null && dataResponse.isSuccess()) {
			LOGGER.info("End fetchAllFromSet for key:{} successful",key);
			return dataResponse.getData();
		}
		LOGGER.info("End fetchAllFromSet for key:{} un-successful",key);
		return new String[0];
	}



}