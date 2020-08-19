package ae.gov.sdg.paperless.platform.cache.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheJsonRequest {

    @ApiModelProperty(
            value = "Key for persisting data.",
            example = "user")
    @JsonProperty("key")
    private String key;

    @ApiModelProperty(
            value = "The json value to be persisted in the cache for the provided key.",
            example = "{	\n" +
                    "		\"fName\": \"John\"\n" +
                    "	}")
    @JsonProperty("value")
    private JSONObject value;

    @ApiModelProperty(value = "Time in minutes for which the key will be available in cache.", required = false, example = "50000")
    @JsonProperty("timeInMinutes")
    private Long timeInMinutes;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public JSONObject getValue() {
        return value;
    }

    public void setValue(final JSONObject value) {
        this.value = value;
    }

    public Long getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(final Long timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("key", key).append("value", value).append("timeInMinutes", timeInMinutes).build();
	}
    
    

}
