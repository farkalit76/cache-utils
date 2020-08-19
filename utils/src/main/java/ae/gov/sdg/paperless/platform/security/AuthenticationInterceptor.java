package ae.gov.sdg.paperless.platform.security;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.BEARER;
import static ae.gov.sdg.paperless.platform.common.PlatformConstants.COLON;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nimbusds.jose.JOSEException;

import ae.gov.sdg.paperless.platform.common.PlatformConfig;
import ae.gov.sdg.paperless.platform.common.model.LOGIN_SOURCE;
import ae.gov.sdg.paperless.platform.common.model.UserInfo;
import ae.gov.sdg.paperless.platform.common.service.authentication.UserInfoService;
import ae.gov.sdg.paperless.platform.exceptions.UserAuthenticationFailedException;

/**
 * This Request Interceptor will validate UAE Pass access token and load user
 * info
 *
 * @author saddam hussain
 *
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 
	 */
	private static final String USER_IS_NOT_VERIFIED = "user is not verified";

	private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

	public static final String HEADER_AUTHORIZATION_SERVICE = "x-ServiceAuthorization";
	public static final String HEADER_AUTHORIZATION_DUBAINOW_JWT = "Authorization";
    public static final String HEADER_AUTHORIZATION_UNIFIED_ACG_ACCESS_TOKEN = "access_token";

    @Autowired
    private PlatformConfig config;

	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
    private JWTService jwtUtil;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		final HandlerMethod handlerMethod = (HandlerMethod) handler;
		final LoginRequired loginRequired = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
		if (loginRequired == null) {
			return true;
		}

        if (config.getActiveProfile() != null && !(config.isDevProfile() || config.isLocalProfile())) {
		   authenticateDubaiNowService(request);
		}


        String dubaiNowJWT = request.getHeader(HEADER_AUTHORIZATION_DUBAINOW_JWT);
        final String unifiedACGAcessToken = request.getHeader(HEADER_AUTHORIZATION_UNIFIED_ACG_ACCESS_TOKEN);
        UserInfo userInfo;
        if (!StringUtils.isEmpty(dubaiNowJWT)) {
            dubaiNowJWT=dubaiNowJWT.trim().replaceFirst(BEARER, EMPTY).trim();
            final Instant start = Instant.now();
            authenticateJWTOffline(dubaiNowJWT);
            final Instant finish = Instant.now();
            final long timeElapsed = Duration.between(start, finish).toMillis();
            log.info("Total Time Spent For offline jwt verification = {}", timeElapsed);
            try {
                userInfo = userInfoService.getUnifiedUserInfo(unifiedACGAcessToken, dubaiNowJWT);
                request.setAttribute("dubaiNowJWTToken", dubaiNowJWT);
                request.setAttribute("dubaiNowAccessToken", unifiedACGAcessToken);
            } catch (final Exception ex) {
                log.error("unable to load unified user info error=", ex);
                throw new UserAuthenticationFailedException("unable to to load unified user info");
            }
            validateUserAccess(request, userInfo, dubaiNowJWT);
            request.setAttribute("userInfo", userInfo);

            return super.preHandle(request, response, handler);
        } else {
            if(HttpMethod.POST.matches(request.getMethod()) || HttpMethod.GET.matches(request.getMethod())) {
                return true;
            }
            throwInvalidTokenException();
        }
        return super.preHandle(request, response, handler);
	}

	private void validateUserAccess(final HttpServletRequest request, final UserInfo userInfo, final String dubaiNowJWT)
			throws UserAuthenticationFailedException, ParseException {
		log.info("Start validate user access");
		final LOGIN_SOURCE loginSource = jwtUtil.getLoginSource(dubaiNowJWT);
		log.info("User login source:{}",loginSource);
		if (userInfo == null) {
            log.error("user info not found");
            throw new UserAuthenticationFailedException("user info not found");
        } else if(loginSource.equals(LOGIN_SOURCE.UAEPASS)) {
			if (!isVerifiedUserCheckRequired(request, userInfo) && !userInfo.isAccountVerfied()) {
		        log.error(USER_IS_NOT_VERIFIED);
		        throw new UserAuthenticationFailedException(USER_IS_NOT_VERIFIED);
		    }
		} else if(loginSource.equals(LOGIN_SOURCE.MPAY) && !isVerifiedUserCheckRequired(request, userInfo)) {
		        log.error(USER_IS_NOT_VERIFIED);
		        throw new UserAuthenticationFailedException(USER_IS_NOT_VERIFIED);
		}
		log.info("End validate user access");
	}

    private boolean isVerifiedUserCheckRequired(final HttpServletRequest request, final UserInfo userInfo) {

        boolean ignoreVerifiedUserCheck = true;
        final Map<String, List<String>> authModesMap = config.getAuthModes().getService();
        final List<String> serviceAuthModes = authModesMap.get(request.getAttribute("journey"));
        if (!serviceAuthModes.contains(userInfo.getUserType())) {
            ignoreVerifiedUserCheck = false;
        }
        return ignoreVerifiedUserCheck;
    }

    private void authenticateJWTOffline(final String dubaiNowJWT) throws UserAuthenticationFailedException, CertificateException, ParseException, JOSEException {
        final Certificate certificate = jwtUtil.loadCertificateFromCertificateFactory(config.getJWTCertificatePath());
        if (!StringUtils.isEmpty(certificate)) {
            if (!jwtUtil.isValidSignature(certificate, dubaiNowJWT)) {
                log.error("Invalid Singature");
                throw new UserAuthenticationFailedException("Signature Mismatch");
            }
            if (!jwtUtil.isValidToken(dubaiNowJWT)) {
                log.error("Invalid Token");
                throw new UserAuthenticationFailedException("Token Expired");
            }
        } else {
            log.error("Unable to load certificate");
            throw new UserAuthenticationFailedException("Unable to load certificate");
        }
    }

	/***
	 * Extra authentication for dubai now service
	 * @param request
	 * @throws UserAuthenticationFailedException
	 */
    private void authenticateDubaiNowService(final HttpServletRequest request) throws UserAuthenticationFailedException {
        // decode base 64
        // validate username/password from config
        // password should be stored as sha512
        // you need to hash the password before comparing

        final String serviceAuthToken = request.getHeader(HEADER_AUTHORIZATION_SERVICE);
		final String decodedserviceAuthToken = new String(Base64.getDecoder().decode(serviceAuthToken));
		final String[] authDetails = decodedserviceAuthToken.split(COLON);
        if (authDetails == null || authDetails.length != 2 || !(config.getDubaiNowServiceUsername().equalsIgnoreCase(authDetails[0])
                && config.getDubaiNowServicePassword().equals(EncryptionUtil.hash(authDetails[1])))) {
		    throwInvalidTokenException();
		}
    }

	public static void throwInvalidTokenException() throws UserAuthenticationFailedException {
	    log.error("missing access token");
        throw new UserAuthenticationFailedException("missing access token");
	}

}
