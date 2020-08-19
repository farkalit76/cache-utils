package ae.gov.sdg.paperless.platform.cache.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheStringResponse extends BaseResponse {

    @ApiModelProperty(
            value = "The response retrieved for the provided key based on operation used.",
            example = "123456")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

}
