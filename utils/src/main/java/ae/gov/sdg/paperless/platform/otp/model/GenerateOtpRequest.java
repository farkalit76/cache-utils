package ae.gov.sdg.paperless.platform.otp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author swetabh raj
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "message", "mobileNum", "otpTimeLimit" })
public class GenerateOtpRequest {

    @JsonProperty("message")
    private String message;
    @JsonProperty("mobileNum")
    private long mobileNum;
    @JsonProperty("otpTimeLimit")
    private long otpTimeLimit;

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("mobileNum")
    public long getMobileNum() {
        return mobileNum;
    }

    @JsonProperty("mobileNum")
    public void setMobileNum(long mobileNum) {
        this.mobileNum = mobileNum;
    }

    @JsonProperty("otpTimeLimit")
    public long getOtpTimeLimit() {
        return otpTimeLimit;
    }

    @JsonProperty("otpTimeLimit")
    public void setOtpTimeLimit(long otpTimeLimit) {
        this.otpTimeLimit = otpTimeLimit;
    }

}