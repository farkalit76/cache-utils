package ae.gov.sdg.paperless.platform.cache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    @ApiModelProperty(
            value = "Indicator of the operation whether successful or not.",
            example = "true")
    @JsonProperty("success")
    private boolean success;

    public BaseResponse() {
    }

    public BaseResponse(final boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public static BaseResponse successResponse() {
        return new BaseResponse(true);
    }

    public static BaseResponse failureResponse() {
        return new BaseResponse(false);
    }

}
