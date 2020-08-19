package ae.gov.sdg.paperless.platform.cache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheStringRequest {

    @ApiModelProperty(
            value = "Key for persisting data.",
            example = "john")
    @JsonProperty("key")
    private String key;

    @ApiModelProperty(
            value = "The json value to be persisted in the cache for the provided key.",
            example = "1234")
    @JsonProperty("value")
    private String value;

    @ApiModelProperty(value = "Time in minutes for which the key will be available in cache.", required = false, example = "50000")
    @JsonProperty("timeInMinutes")
    private Long timeInMinutes;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Long getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(final Long timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }
}
