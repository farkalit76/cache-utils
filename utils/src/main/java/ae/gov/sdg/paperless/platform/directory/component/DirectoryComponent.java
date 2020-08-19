package ae.gov.sdg.paperless.platform.directory.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Holds generic data for landing page.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DirectoryComponent implements Serializable {
	
	private static final long serialVersionUID = 4120781331783242448L;

	@ApiModelProperty(value = "Unique identifier of the row", required = false)
	private String id;

	@ApiModelProperty(value = "Image uri", required = false)
	private String icon;

	@ApiModelProperty(value = "Title of the row", required = false)
	private String title;

	@ApiModelProperty(value = "Detail of the row", required = false)
	private String detail;

	@JsonProperty("iconOb")
	@ApiModelProperty(value = "Icon obj of the row", required = false)
	private Object iconOb;

	@JsonProperty("titleOb")
	@ApiModelProperty(value = "Title obj of the row", required = false)
	private Object titleOb;

	@JsonProperty("detailOb")
	@ApiModelProperty(value = "Detail obj of the row", required = false)
	private Object detailOb;

	@ApiModelProperty(value = "Sub detail of the row", required = false)
	private String subDetail;

	@JsonProperty("subDetailOb")
	@ApiModelProperty(value = "Detail obj of the row", required = false)
	private Object subDetailOb;

	@ApiModelProperty(value = "Description of the row", required = false)
	private String description;

	@ApiModelProperty(value = "Action indicator", required = false)
	private Boolean isActionable;

	@ApiModelProperty(value = "Name of action", required = false)
	private String name;

	@ApiModelProperty(value = "Value of action", required = false)
	private Object value;

	@ApiModelProperty(value = "List of actions", required = false)
	private List<Action> actions;

	@ApiModelProperty(value = "Additional attributes of row", required = false)
	private List<AttributeDetail> attributes;

	@ApiModelProperty(value = "Params for submitting to the server", required = false)
	private List<NameValuePair> params;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Object getTitleOb() {
		return titleOb;
	}

	public void setTitleOb(Object titleOb) {
		this.titleOb = titleOb;
	}

	public Object getDetailOb() {
		return detailOb;
	}

	public void setDetailOb(Object detailOb) {
		this.detailOb = detailOb;
	}

	public String getSubDetail() {
		return subDetail;
	}

	public void setSubDetail(String subDetail) {
		this.subDetail = subDetail;
	}

	public Object getSubDetailOb() {
		return subDetailOb;
	}

	public void setSubDetailOb(Object subDetailOb) {
		this.subDetailOb = subDetailOb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActionable() {
		return isActionable;
	}

	public void setIsActionable(Boolean isActionable) {
		this.isActionable = isActionable;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<AttributeDetail> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeDetail> attributes) {
		this.attributes = attributes;
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

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}
}