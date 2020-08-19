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
@JsonPropertyOrder({ "key", "userOtp" })
public class ValidateOtpRequest {

    @JsonProperty("key")
    private String key;
    @JsonProperty("userOtp")
    private long userOtp;

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("userOtp")
    public long getUserOtp() {
        return userOtp;
    }

    @JsonProperty("userOtp")
    public void setUserOtp(long userOtp) {
        this.userOtp = userOtp;
    }

}