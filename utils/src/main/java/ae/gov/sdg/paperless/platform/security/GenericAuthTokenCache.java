package ae.gov.sdg.paperless.platform.security;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ae.gov.sdg.paperless.platform.common.model.GenericAuthToken;

/**
 * @author swetabh raj
 */
public class GenericAuthTokenCache {

    private static final Logger log = LoggerFactory.getLogger(GenericAuthTokenCache.class);

    private static GenericAuthTokenCache instance = new GenericAuthTokenCache();

    private ConcurrentHashMap<String, GenericAuthToken> cachedTokens = null;

    public static GenericAuthTokenCache getInstance() {
        return instance;
    }

    private GenericAuthTokenCache() {
        cachedTokens = new ConcurrentHashMap<>();
    }

    public synchronized GenericAuthToken getToken(final String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        final GenericAuthToken token = cachedTokens.get(name);
        if (token != null && isExpiredToken(token)) {
            cachedTokens.remove(name);
            return null;
        }

        return token;
    }

    public synchronized void addToken(final String name, final GenericAuthToken token) {
        if (StringUtils.isEmpty(name) || token == null || isExpiredToken(token)) {
            return;
        }

        cachedTokens.put(name, token);
    }

    public synchronized void clearAllTokens() {
        cachedTokens.clear();
    }

    private boolean isExpiredToken(final GenericAuthToken token) {
        if (token == null || token.getCreatedDate() == null || token.getExpiresIn() < 0) {
            log.error("The token Expired: {}", token);
            return true;
        }

        final Date current = new Date();
        // We are reducing 5 mins from the calculated expiry time because GSB token expires in 55 mins instead of 60
        final Date expiryDate = DateUtils.addSeconds(token.getCreatedDate(), ((int) token.getExpiresIn() - 300));


        return expiryDate.before(current);
    }
    /*
     * To clear the inactive tokens.
     */
    public synchronized void clearToken(final String name) {
        try { 
            cachedTokens.remove(name);
        }catch(final Exception ex) {
        	log.error("Error while clearing token - {}", ex.getMessage());
        }
    }
}