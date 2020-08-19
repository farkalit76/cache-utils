package ae.gov.sdg.paperless.platform.directory.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author c_chandra.bommise
 *
 * Holds the name and the value of action.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NameValuePair {

    @ApiModelProperty(
        value = "Name of the action",
        required = false)
    private String name;

    @ApiModelProperty(
        value = "Value of the action",
        required = false)
    private Object value;

    public NameValuePair(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public NameValuePair() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

	public void setValue(Object value) {
		this.value = value;
	}


}
