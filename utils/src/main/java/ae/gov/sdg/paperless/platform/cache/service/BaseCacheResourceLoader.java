package ae.gov.sdg.paperless.platform.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.BEARER;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.DUBAI_NOW_ACCESS_TOKEN;

import ae.gov.sdg.paperless.platform.common.model.BearerAuth;
import ae.gov.sdg.paperless.platform.common.model.GenericAuthToken;
import ae.gov.sdg.paperless.platform.common.model.IServiceConfig;
import ae.gov.sdg.paperless.platform.common.service.authentication.GenericAuthenticationService;
import ae.gov.sdg.paperless.platform.common.service.generic.IRestService;

/**
 * @author c_chandra.bommise
 * 
 *         The Class BaseResourceLoader.
 *
 */
public abstract class BaseCacheResourceLoader<T, K, V> implements ICacheResourceLoader<K, V> {

	@Autowired
	@Qualifier("restBearerAuthService")
	private IRestService<BearerAuth> restUtil;

	private GenericAuthenticationService genAuthService;

	@Autowired
	private T cacheService;

	/**
	 * Gets the json cache service.
	 *
	 * @return the json cache service
	 */
	protected T getCacheService() {
		return cacheService;
	}

	/**
	 * Sets the json cache service.
	 *
	 * @param jsonCacheService the new json cache service
	 */
	protected void setCacheService(final T cacheService) {
		this.cacheService = cacheService;
	}

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	protected abstract IServiceConfig getConfig();

	protected IRestService<BearerAuth> getRestUtil() {
		return restUtil;
	}

	@Autowired
	@Qualifier("restBearerAuthService")
	protected void setRestUtil(final IRestService<BearerAuth> restUtil) {
		this.restUtil = restUtil;
	}

	/**
	 * Gets the gen auth service.
	 *
	 * @return the gen auth service
	 */
	protected GenericAuthenticationService getGenAuthService() {
		return genAuthService;
	}

	/**
	 * Sets the gen auth service.
	 *
	 * @param genAuthService the new gen auth service
	 */
	@Autowired
	protected void setGenAuthService(final GenericAuthenticationService genAuthService) {
		this.genAuthService = genAuthService;
	}

	/**
	 * Fetch client access token.
	 *
	 * @return the string
	 */
	protected String fetchClientAccessToken() {
		final GenericAuthToken dubaiNowAccessInfo = getGenAuthService().getOAuthAccessToken(DUBAI_NOW_ACCESS_TOKEN,
				getConfig().getClientId(), getConfig().getClientSecret());
		return BEARER + dubaiNowAccessInfo.getIdToken();
	}

}
