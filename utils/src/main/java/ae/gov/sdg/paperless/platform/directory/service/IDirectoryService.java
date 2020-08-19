package ae.gov.sdg.paperless.platform.directory.service;

import java.io.IOException;

import ae.gov.sdg.paperless.platform.common.model.UserInfo;
import ae.gov.sdg.paperless.platform.directory.component.DirectoryComponentResponse;
import ae.gov.sdg.paperless.platform.directory.component.DirectoryRequest;

/**
 * The Interface IDirectoryService.
 */
public interface IDirectoryService {

	/**
	 * Listing.
	 *
	 * @param request the request
	 * @param userInfo the user info
	 * @param dubaiNowToken the dubai now token
	 * @return the directory component response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	DirectoryComponentResponse listing(DirectoryRequest request, UserInfo userInfo, String dubaiNowToken) throws IOException;

	/**
	 * Operation.
	 *
	 * @return the string
	 */
	String operation();

}