package ae.gov.sdg.paperless.platform.directory.component;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

/**
 * To hold the dependent component of action.
 * 
 * @author c_chandra.bommise
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionDependentComponent {
	
	@ApiModelProperty(
			value = "The name of the dependent component",
			required = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
