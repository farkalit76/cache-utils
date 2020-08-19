package ae.gov.sdg.paperless.platform.cache.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheStringHashCollectionResponse extends BaseResponse {

    @ApiModelProperty(value = "Key values for persisting data.", example = "{\n" +
            "	\"john\" : \"1234\",\n" +
            "	\"davis\" : \"0361\",\n" +
            "   \"sean\" : \"8295\"\n" +
            "}")
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(final Map<String, Object> data) {
        this.data = data;
    }

}