package ae.gov.sdg.paperless.platform.directory.component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Holds the specific response for directory data list.
 * 
 * @author c_chandra.bommise
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DirectoryComponentResponse extends DirectoryResponse<DirectoryComponent> {

	private static final long serialVersionUID = 1941035314714786717L;

}