package ae.gov.sdg.paperless.platform.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheHashKeysResponse extends BaseResponse {

    @ApiModelProperty(value = "Key values for persisting data.", example = "")
    private Set<String> data;

    public Set<String> getData() {
        return data;
    }

    public void setData(final Set<String> data) {
        this.data = data;
    }

}
