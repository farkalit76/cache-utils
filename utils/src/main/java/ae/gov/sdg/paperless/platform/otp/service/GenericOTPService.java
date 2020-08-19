package ae.gov.sdg.paperless.platform.otp.service;

import java.io.IOException;

import ae.gov.sdg.paperless.platform.otp.model.ValidateOtpResponse;

/**
 * @author swetabh raj
 *
 */
public interface GenericOTPService {

    String generateOTP(String message, long mobileNumber, long otpTimeLimit) throws IOException;

    ValidateOtpResponse validateOTP(String uuid, long otp);

}