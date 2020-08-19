package ae.gov.sdg.paperless.platform.directory.component;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Holds the directory response.
 * 
 * @author c_chandra.bommise
 *
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DirectoryResponse<T> implements Serializable {

	private static final long serialVersionUID = 349905245139746555L;

	@ApiModelProperty(value = "The total number of pages", example = "5")
	@JsonProperty("totalPages")
	private Integer totalPages;

	@ApiModelProperty(value = "The total number of records", example = "75")
	@JsonProperty("totalRecords")
	private Integer totalRecords;

	@ApiModelProperty(value = "The server has more records to process", example = "true")
	@JsonProperty("hasMoreRecords")
	private Boolean hasMoreRecords;

	@ApiModelProperty(value = "The status returned from server", example = "400")
	@JsonProperty("status")
	private Integer status;

	@ApiModelProperty(value = "The message returned from server", example = "Successful response")
	@JsonProperty("message")
	private String message;

	/*
	 * @ApiModelProperty( value =
	 * "The parms that will be submitted in subsequent requests")
	 * 
	 * @JsonProperty("params") private Map<String,Object> params;
	 */

	@ApiModelProperty(value = "The parms that will be submitted in subsequent requests")
	@JsonProperty("params")
	private List<NameValuePair> params;

	@ApiModelProperty(value = "The message returned from server. One of data or components must be set.", example = "Successful response")
	@JsonProperty("data")
	private List<T> data;

	@ApiModelProperty(value = "The message returned from server. One of data or components must be set.", example = "Successful response")
	@JsonProperty("components")
	private List<T> components;

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Boolean getHasMoreRecords() {
		return hasMoreRecords;
	}

	public void setHasMoreRecords(Boolean hasMoreRecords) {
		this.hasMoreRecords = hasMoreRecords;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public List<T> getComponents() {
		return components;
	}

	public void setComponents(List<T> components) {
		this.components = components;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}
}
