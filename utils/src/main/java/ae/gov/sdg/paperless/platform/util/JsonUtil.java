package ae.gov.sdg.paperless.platform.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * The Class JsonUtil.
 */
public final class JsonUtil {

    /**
     * Private Default constructor to prevent initialization from this class.
     */
    private JsonUtil() {
    }

    private static final ObjectMapper objectMapper;

    static {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper = mapper;
    }

    /**
     * To safe json string.
     *
     * @param obj the obj
     * @return the string
     * @throws JsonProcessingException 
     */
    public static String toSafeJsonString(final Object obj) throws JsonProcessingException {
    	return toJson(obj, true);
    }

    /**
     * To json.
     *
     * @param object the object
     * @return the string
     * @throws JsonProcessingException 
     */
    public static String toJson(final Object object) throws JsonProcessingException {
        return toJson(object, false);
    }
    
    /**
     * Object with JSON with pretty print feature.
     *
     * @param object the object
     * @param pretty the pretty
     * @return the string
     */
    public static String toJson(final Object object, final boolean pretty) throws JsonProcessingException {
		return pretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object)
				: objectMapper.writeValueAsString(object);
    }
    
    /**
     * To map.
     *
     * @param json the json
     * @return the map
     * @throws JsonProcessingException 
     */
    public static Map<String, String> toMap(final String json) throws JsonProcessingException {
        final TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
        };
        return fromJson(json, typeRef);
    }
    
    /**
     * From stream.
     *
     * @param <T> the generic type
     * @param in the in
     * @param outputType the output type
     * @return the t
     */
    public static <T> T fromStream(final InputStream in, final Class<T> outputType) throws IOException {
        return objectMapper.readValue(in, outputType);
    }

    /**
     * Object from JSON.
     *
     * @param <T> the generic type
     * @param <R> the generic type
     * @param json the json
     * @param responseType the response type
     * @return the r
     */
    @SuppressWarnings("rawtypes")
	public static <T, R> R fromJson(final T json, final Class<R> responseType) throws JsonProcessingException {
		if (json != null) {
        	if(json instanceof JSONArray) {
        		return objectMapper.readValue(((JSONArray)json).toJSONString(), responseType);
        	} else if(json instanceof JSONObject) {
        		return objectMapper.readValue(((JSONObject)json).toJSONString(), responseType);
        	} else if(json instanceof org.json.JSONArray) {
        		return objectMapper.readValue(((org.json.JSONArray)json).toString(), responseType);
        	} else if(json instanceof org.json.JSONObject) {
        		return objectMapper.readValue(((org.json.JSONObject)json).toString(), responseType);
        	} else if(json instanceof org.json.simple.JSONArray) {
        		return objectMapper.convertValue((org.json.simple.JSONArray)json, responseType);
        	} else if(json instanceof org.json.simple.JSONObject) {
        		return objectMapper.readValue(((org.json.simple.JSONObject)json).toString(), responseType);
        	} else if(json instanceof LinkedHashMap) {
        		return objectMapper.convertValue((LinkedHashMap)json, responseType);
        	} else if(json instanceof String) {
        		return objectMapper.readValue((String)json, responseType);
        	}
    	}
    	return null;
    }

    /**
     * From json.
     *
     * @param in the in
     * @return the map
     * @throws IOException 
     */
    public static Map<String, Object> fromJson(final InputStream in) throws IOException {
        final TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
        };
        return fromJson(in, typeRef);
    }

    /**
     * From json.
     *
     * @param <T> the generic type
     * @param in the in
     * @param typeRef the type ref
     * @return the t
     */
    public static <T> T fromJson(final InputStream in, final TypeReference<T> typeRef) throws IOException {
        return objectMapper.readValue(in, typeRef);
    }

    /**
     * From json.
     *
     * @param json the json
     * @return the map
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Map<String, Object> fromJson(final String json) throws IOException {
			return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
		});
    }

    /**
     * From json.
     *
     * @param <T> the generic type
     * @param json the json
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T fromJson(final String json, final TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(json, typeReference);
    }

    /**
     * Object from JSON.
     *
     * @param <T> the generic type
     * @param json the json
     * @param jsonClass the json class
     * @param extraParams the extra params
     * @return the t
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static <T> T fromJson(final String json, final Class<T> jsonClass, final boolean extraParams) throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        if (extraParams) {
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        }
        return mapper.readValue(json, jsonClass);
    }

    /**
     * Json to list.
     *
     * @param json the json
     * @return the list
     * @throws JsonProcessingException 
     */
    public static List<Map<String, Object>> jsonToList(final String json) throws JsonProcessingException {
        final TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {
        };
        return fromJson(json, typeRef);
    }
    
    /**
     * List of objects from JSON.
     *
     * @param <T> the generic type
     * @param json the json
     * @param typeReference the type reference
     * @return list
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static <T> List<T> jsonToList(final String json, final TypeReference<List<T>> typeReference) throws IOException {
		return objectMapper.readValue(json, typeReference);
	}
    
    /**
     * Json to map.
     *
     * @param json the json
     * @return the map
     * @throws JsonProcessingException 
     */
    public static Map<String, Object> jsonToMap(final String json) throws JsonProcessingException {
        final TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
        };
        return fromJson(json, typeRef);
    }
    
    /**
     * check if the passed json obj is valid Json.
     *
     * @param obj the obj
     * @return true, if is valid json sring
     */
    public static boolean isValidJsonSring(final String obj) {
        if (obj == null) {
            return false;
        }
        try {
        	objectMapper.readTree(obj);
            return true;
        } catch (final IOException e) {
            return false;
        }
    }
    
    /**
     * Typereference object from JSON.
     *
     * @param <T> the generic type
     * @param <R> the generic type
     * @param json the json
     * @param responseType the response type
     * @return the r
     */
    @SuppressWarnings("rawtypes")
	public static <T, R> R fromJson(final T json, final TypeReference<R> responseType) throws JsonProcessingException {
		if (json != null) {
        	if(json instanceof JSONArray) {
        		return objectMapper.readValue(((JSONArray)json).toJSONString(), responseType);
        	} else if(json instanceof JSONObject) {
        		return objectMapper.readValue(((JSONObject)json).toJSONString(), responseType);
        	} else if(json instanceof org.json.JSONArray) {
        		return objectMapper.readValue(((org.json.JSONArray)json).toString(), responseType);
        	} else if(json instanceof org.json.JSONObject) {
        		return objectMapper.readValue(((org.json.JSONObject)json).toString(), responseType);
        	} else if(json instanceof org.json.simple.JSONArray) {
        		return objectMapper.convertValue((org.json.simple.JSONArray)json, responseType);
        	} else if(json instanceof org.json.simple.JSONObject) {
        		return objectMapper.readValue(((org.json.simple.JSONObject)json).toString(), responseType);
        	} else if(json instanceof LinkedHashMap) {
        		return objectMapper.convertValue((LinkedHashMap)json, responseType);
        	} else if(json instanceof String) {
        		return objectMapper.readValue((String)json, responseType);
        	}
    	}
    	return null;
    }
    
    /**
     * Convert json to list.
     *
     * @param <T> the generic type
     * @param json the json
     * @return the list
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static <T> List<T> convertJsonToList(String json) throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
			return mapper.readValue(json, new TypeReference<List<T>>() {
		});
	}

}