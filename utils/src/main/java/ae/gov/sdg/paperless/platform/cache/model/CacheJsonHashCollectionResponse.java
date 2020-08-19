package ae.gov.sdg.paperless.platform.cache.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheJsonHashCollectionResponse extends BaseResponse {

    @ApiModelProperty(value = "Key values for persisting data.", example = "{\n" +
            "	\"john\" : {\n" +
            "		    \"fName\":\"John\",\n" +
            "	        \"lName\": \"Davis\"\n" +
            "	}\n" +
            "}")
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(final Map<String, Object> data) {
        this.data = data;
    }

}