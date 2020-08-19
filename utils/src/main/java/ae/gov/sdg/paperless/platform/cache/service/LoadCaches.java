package ae.gov.sdg.paperless.platform.cache.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author c_chandra.bommise
 *         <p>
 *         Load the caches on server startup.
 */
@Component
@Profile("!test")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LoadCaches {
	// Logger instance
	private final Logger log = LoggerFactory.getLogger(LoadCaches.class);

	private final List<ICacheResourceLoader> cacheLoaders;

	/**
	 * Autowire all the cache loaders that implement ICacheResourceLoader.
	 *
	 * @param cacheLoaders
	 */
	@Autowired
	public LoadCaches(final List<ICacheResourceLoader> cacheLoaders) {
		this.cacheLoaders = cacheLoaders;
	}

	/**
	 * Load the caches.
	 */
	@PostConstruct
	public void init() {
		log.info("Loading all the caches on startup");
		cacheLoaders.stream().filter(l -> (l instanceof LoadOnStartup) && !l.exists(l.getKey()))
				.forEach(ICacheResourceLoader::execute);
	}

}
