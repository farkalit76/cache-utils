package ae.gov.sdg.paperless.platform.directory.component;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author c_chandra.bommise
 * 
 * Holds the action on the data.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action implements Serializable{

	private static final long serialVersionUID = -8286953246965344798L;

	@ApiModelProperty(
			value = "Title of action",
			required = false)
	private String title;
	
	@ApiModelProperty(
			value = "Type of action",
			required = false)
	private String type;
	
	@ApiModelProperty(
			value = "Is actionable",
			required = false)
	private Boolean action;
	
	@ApiModelProperty(
			value = "List of action details",
			required = false)
	private List<ActionDetail> data;
	
	@ApiModelProperty(
			value = "List of action details",
			required = false)
	private List<ActionDependencies> dependencies;
	
	@ApiModelProperty(
			value = "Name of action",
			required = false)
	private String name;

	@ApiModelProperty(
			value = "Value of action",
			required = false)
	private String value;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getAction() {
		return action;
	}

	public void setAction(Boolean action) {
		this.action = action;
	}

	public List<ActionDetail> getData() {
		return data;
	}

	public void setData(List<ActionDetail> data) {
		this.data = data;
	}

	public List<ActionDependencies> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<ActionDependencies> dependencies) {
		this.dependencies = dependencies;
	}

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


}
