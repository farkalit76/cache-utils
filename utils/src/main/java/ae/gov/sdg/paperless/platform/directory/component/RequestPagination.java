package ae.gov.sdg.paperless.platform.directory.component;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author c_chandra.bommise
 * 
 * Holds the pagination info of the request.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class RequestPagination implements Serializable {

	private static final long serialVersionUID = -713169579828906701L;




}
