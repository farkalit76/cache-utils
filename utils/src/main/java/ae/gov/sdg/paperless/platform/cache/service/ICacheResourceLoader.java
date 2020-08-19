package ae.gov.sdg.paperless.platform.cache.service;

import java.io.IOException;

/**
 * @author c_chandra.bommise
 *
 * @param <K>
 * @param <V>
 * 
 *            Interface for loading the data into cache
 */
public interface ICacheResourceLoader<K, V> {

	void execute();

	V getItem(K key) throws IOException;

	boolean exists(K key);

	K getKey();
}
