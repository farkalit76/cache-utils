package ae.gov.sdg.paperless.platform.directory.component;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeDetail implements Serializable {

	private static final long serialVersionUID = -1051750627276767712L;

	@ApiModelProperty(value = "Name of the attribute", required = false)
	private String name;

	@ApiModelProperty(value = "Value of the attribute", required = false)
	private String value;

	@ApiModelProperty(value = "Property name of the attribute", required = false)
	private String propName;

	@ApiModelProperty(value = "Image uri of the attribute", required = false)
	private String icon;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}