package ae.gov.sdg.paperless.platform.cache.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheJsonHashRequest extends CacheJsonRequest {

    @ApiModelProperty(
            value = "Collection for persisting data.",
            example = "users")
    @JsonProperty("keySpace")
    private String keySpace;

    public String getKeySpace() {
        return keySpace;
    }

    public void setKeySpace(final String keySpace) {
        this.keySpace = keySpace;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("key", getKey()).append("keySpace", keySpace).build();
	}

    
    
}
