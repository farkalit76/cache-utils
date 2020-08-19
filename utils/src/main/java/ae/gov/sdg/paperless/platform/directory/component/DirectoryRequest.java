package ae.gov.sdg.paperless.platform.directory.component;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import ae.gov.sdg.paperless.platform.common.model.components.types.LangType;
import ae.gov.sdg.paperless.platform.util.JsonUtil;
import io.swagger.annotations.ApiModelProperty;

/**
 * Holds the directory request.
 * 
 * @author c_chandra.bommise
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DirectoryRequest implements Serializable {

	private static final long serialVersionUID = 1108018725280215085L;

	@ApiModelProperty(value = "The limit on the number of records as part of response", example = "15", required = false)
	@JsonProperty("limit")
	private Integer limit;

	@ApiModelProperty(value = "The current page number", example = "0", required = true)
	@JsonProperty("page")
	private Integer page;

	private LangType locale;

	@ApiModelProperty(value = "The current screen handled by the user", example = "StartJourneyScreen", required = false)
	@JsonProperty("sequence")
	private String screen;

	@ApiModelProperty(value = "The current component handled by the user", example = "DrivingLicenseChoice", required = false)
	@JsonProperty("component")
	private String component;

	@ApiModelProperty(value = "Dictionary of posted parameters", notes = "If the user clicks on a component with action=true then this will contain the "
			+ "name/value of the component. If this is from a form submit then it will include"
			+ "all the form parameter names and values", required = false)
	@JsonProperty("params")
	private Map<String, Object> params;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public LangType getLocale() {
		return locale;
	}

	public void setLocale(LangType locale) {
		this.locale = locale;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String toJsonString() {
		try {
			return JsonUtil.toJson(this, true);
        } catch (JsonProcessingException e) {
            //ignore
        }
        return EMPTY;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

}