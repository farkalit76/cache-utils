package ae.gov.sdg.paperless.platform.otp.service;

import static ae.gov.sdg.paperless.platform.common.service.generic.RestBasicAuthService.authHeaders;

import java.io.IOException;
import java.net.URI;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ae.gov.sdg.paperless.platform.common.PlatformConfig;
import ae.gov.sdg.paperless.platform.common.model.BasicAuth;
import ae.gov.sdg.paperless.platform.common.service.generic.IRestService;
import ae.gov.sdg.paperless.platform.otp.model.GenerateOtpRequest;
import ae.gov.sdg.paperless.platform.otp.model.GenerateOtpResponse;
import ae.gov.sdg.paperless.platform.otp.model.ValidateOtpRequest;
import ae.gov.sdg.paperless.platform.otp.model.ValidateOtpResponse;
import ae.gov.sdg.paperless.platform.util.JsonUtil;

@Profile("!stub")
@Service
/**
 * @author swetabh raj
 *
 */
public class GenericOTPServiceImpl implements GenericOTPService {

    private static final Logger log = LoggerFactory.getLogger(GenericOTPServiceImpl.class);

    private IRestService<BasicAuth> restTemplate;

    private PlatformConfig config;
    
    private BasicAuth basicAuth;
    
    public GenericOTPServiceImpl(final @Qualifier("commonServicesRestTemplate") IRestService<BasicAuth> restTemplate, final PlatformConfig config) {
		super();
		this.restTemplate = restTemplate;
		this.config = config;
	}
    
    /**
	 * Post constructor for initializing URL.
	 */
    @PostConstruct
    public void init() {
    	basicAuth = new BasicAuth(config.getDubaiNowCacheUserName(), config.getDubaiNowCachePassword());
    }

    @Override
    public String generateOTP(String message, long mobileNumber, long otpTimeLimit) throws IOException {

        GenerateOtpRequest otpRequest = new GenerateOtpRequest();
        log.info("generateOTP method called");
        otpRequest.setMessage(message);
        otpRequest.setMobileNum(mobileNumber);
        if (otpTimeLimit > 0L) {
            otpRequest.setOtpTimeLimit(otpTimeLimit);
        }
        RequestEntity<GenerateOtpRequest> requestEntity = RequestEntity
                .post(URI.create(config.getDubaiNowCacheContextUrl().concat("api/otp/send"))).accept(MediaType.ALL)
                .headers(authHeaders(basicAuth)).body(otpRequest);
        ResponseEntity<String> response = restTemplate.invoke(requestEntity, String.class);
        GenerateOtpResponse dataResponse = JsonUtil.fromJson(response.getBody(), GenerateOtpResponse.class);
        if (response != null && dataResponse.getSuccess()) {
        	log.info("Success Response from GenericOTPServiceImpl generateOTP method");
            return dataResponse.getUuid();
        }
        return null;
    }

    @Override
    public ValidateOtpResponse validateOTP(String uuid, long otp) {

        ValidateOtpRequest otpRequest = new ValidateOtpRequest();
        log.info("validateOTP method called");
        otpRequest.setKey(uuid);
        otpRequest.setUserOtp(otp);
        RequestEntity<ValidateOtpRequest> requestEntity = RequestEntity
                .post(URI.create(config.getDubaiNowCacheContextUrl().concat("api/otp/validate")))
                .accept(MediaType.ALL).headers(authHeaders(basicAuth))
                .body(otpRequest);
        ResponseEntity<ValidateOtpResponse> response = restTemplate.invoke(requestEntity, ValidateOtpResponse.class);
        if (response != null && response.getBody() != null) {
        	log.info("Success Response from GenericOTPServiceImpl validateOTP method");
            return response.getBody();
        }
        return null;
    }

}