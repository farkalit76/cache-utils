package ae.gov.sdg.paperless.platform.cache.model;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheJsonListRequest {

    @ApiModelProperty(
            value = "Key for persisting data.",
            example = "users")
    @JsonProperty("key")
    private String key;

    @ApiModelProperty(
            value = "The json value to be persisted in the cache for the provided key.",
            example = "[\n" +
                    "    {\n" +
                    "      \"user1\": {\n" +
                    "        \"name\": \"John\",\n" +
                    "        \"age\": \"30\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"user2\": {\n" +
                    "        \"name\": \"Davis\",\n" +
                    "        \"age\": \"59\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"user3\": {\n" +
                    "        \"name\": \"Joe\",\n" +
                    "        \"age\": \"22\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]")
    @JsonProperty("value")
    private JSONObject[] value;

    @ApiModelProperty(value = "Time in minutes for which the key will be available in cache.", required = false, example = "50000")
    @JsonProperty("timeInMinutes")
    private Long timeInMinutes;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public JSONObject[] getValue() {
        return value;
    }

    public void setValue(final JSONObject[] value) {
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
		return new ToStringBuilder(this).append("key", key).append("value", Arrays.toString(value)).append("timeInMinutes", timeInMinutes).build();
	}

}
