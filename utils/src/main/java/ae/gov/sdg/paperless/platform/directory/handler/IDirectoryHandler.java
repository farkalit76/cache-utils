package ae.gov.sdg.paperless.platform.directory.handler;

import ae.gov.sdg.paperless.platform.directory.service.IDirectoryService;

/**
 * The Interface IDirectoryHandler.
 *
 * @author c_chandra.bommise
 */
public interface IDirectoryHandler {

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType();

	/**
	 * Directory service.
	 *
	 * @param operation the operation
	 * @return the i directory service
	 */
	public IDirectoryService directoryService(String operation);
}