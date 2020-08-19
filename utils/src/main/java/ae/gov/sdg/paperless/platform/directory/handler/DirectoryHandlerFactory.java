package ae.gov.sdg.paperless.platform.directory.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * A factory for creating DirectoryHandler objects.
 *
 * @author c_chandra.bommise
 * 
 * Factory for obtaining the appropriate entity for handling requests.
 */
@Component
public class DirectoryHandlerFactory{

	private final List<IDirectoryHandler> entityHandlers;

	@Autowired
	public DirectoryHandlerFactory(List<IDirectoryHandler> entityHandlers) {
		this.entityHandlers = entityHandlers;
	}

	/**
	 * Fetch the appropriate implementation classes based on entity type.
	 *
	 * @param type the type
	 * @return the i directory handler
	 */
	public IDirectoryHandler directoryHandler(String type) {
		Optional<IDirectoryHandler> optionalDirectoryHandler = entityHandlers.stream()
				.filter(entityHandler -> entityHandler.getType().equals(type)).findAny();
		if (optionalDirectoryHandler.isPresent()) {
			return optionalDirectoryHandler.get();
		}
		return null;
	}
}