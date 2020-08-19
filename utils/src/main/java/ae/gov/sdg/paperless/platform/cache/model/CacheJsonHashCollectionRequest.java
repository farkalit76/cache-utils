package ae.gov.sdg.paperless.platform.cache.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheJsonHashCollectionRequest {

    @ApiModelProperty(
            value = "Collection for persisting data.",
            example = "users")
    @JsonProperty("keySpace")
    private String keySpace;

    @ApiModelProperty(value = "Key values for persisting data.", example = "{\n" +
            "	\"john\" : {\n" +
            "		    \"fName\":\"John\",\n" +
            "	        \"lName\": \"Davis\"\n" +
            "	}\n" +
            "}")
    private Map<String, Object> cacheMap;

    @ApiModelProperty(value = "Time in minutes for which the key will be available in cache.", required = false, example = "50000")
    @JsonProperty("timeInMinutes")
    private Long timeInMinutes;

    public String getKeySpace() {
        return keySpace;
    }

    public void setKeySpace(final String keySpace) {
        this.keySpace = keySpace;
    }

    public Map<String, Object> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(final Map<String, Object> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public Long getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(final Long timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("keySpace", keySpace).append("cacheMap", cacheMap).append("timeInMinutes", timeInMinutes).build();
	}
    
    
}
