package ae.gov.sdg.paperless.platform.directory.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author c_chandra.bommise
 * 
 * Holds the details of the action.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionDetail {
	
	List<NameValuePair> params;

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}
	
}
