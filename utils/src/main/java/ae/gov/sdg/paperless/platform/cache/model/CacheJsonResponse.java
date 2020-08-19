package ae.gov.sdg.paperless.platform.cache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheJsonResponse extends BaseResponse {

    @ApiModelProperty(
            value = "The json response retrieved for the provided key based on operation used.",
            example = "{	\n" +
                    "		\"test\": \"1\"\n" +
                    "	}")
    @JsonProperty("data")
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(final Object data) {
        this.data = data;
    }

}
