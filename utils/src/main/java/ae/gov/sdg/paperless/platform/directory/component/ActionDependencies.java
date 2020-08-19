package ae.gov.sdg.paperless.platform.directory.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

/**
 * Holds the action dependencies.
 * @author c_chandra.bommise
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionDependencies {
	
	@ApiModelProperty(
			value = "Criteria of the action dependency",
			required = false)
	private String criteria;
	
	@ApiModelProperty(
			value = "The action dependency",
			required = false)
	private String action;
	
	@ApiModelProperty(
			value = "The list of dependent components",
			required = false)
	private List<ActionDependentComponent> dependentComponents;

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<ActionDependentComponent> getDependentComponents() {
		return dependentComponents;
	}

	public void setDependentComponents(List<ActionDependentComponent> dependentComponents) {
		this.dependentComponents = dependentComponents;
	}
	
	

}
