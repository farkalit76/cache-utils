package ae.gov.sdg.paperless.platform.tracing.events.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ae.gov.sdg.paperless.platform.util.JsonUtil;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEventsRequest {
	
	@ApiModelProperty(
            value = "Request specific unique id used for tracing the request flow.",
            example = "DN2221-YhJuYQ")
	@JsonProperty("traceId")
	private String traceId;

	@ApiModelProperty(
            value = "Session id of the user.",
            example = "1956554a-f540-41b2-8b98-9ae92afba287")
	@JsonProperty("sessionId")
	private String sessionId;
	
	@ApiModelProperty(
            value = "Name of the application from which event is published.",
            example = "DubaiNow")
	@JsonProperty("appName")
	private String appName;

	@ApiModelProperty(
            value = "Version of the application.",
            example = "6.4-dev")
	@JsonProperty("appVersion")
	private String appVersion;
	
	@ApiModelProperty(
            value = "Language used for the request.",
            example = "en")
	@JsonProperty("locale")
	private String locale;
	
	@ApiModelProperty(
            value = "Email of the user.",
            example = "abc@gmail.com")
	@JsonProperty("email")
	private String email;

	@ApiModelProperty(
            value = "Login type of the user.",
            example = "UAEPass")
	@JsonProperty("loginType")
	private String loginType;
	
	@ApiModelProperty(
            value = "Source from which the request event is being published.",
            example = "Mobile")
	@JsonProperty("source")
	private String source;

	@ApiModelProperty(
            value = "Service for which event is being published.",
            example = "Real Estate Brokers")
	@JsonProperty("service")
	private String service;

	@ApiModelProperty(
            value = "Screen name for which event is published with path.",
            example = "Rent -> Rent -> Buy -> Rent -> Rent -> Buy -> parking_permit-template-screen -> rah-simsari-property-list-template-screen")
	@JsonProperty("screenName")
	private String screenName;

	@ApiModelProperty(
            value = "Resource of published event.",
            example = "App")
	@JsonProperty("resource")
	private String resource;
	
	@ApiModelProperty(
            value = "Type of event.",
            example = "ERROR")
	@JsonProperty("instrumentType")
	private String instrumentType;

	@ApiModelProperty(
            value = "Network type on which it was connected.",
            example = "WIFI")
	@JsonProperty("networkType")
	private String networkType;

	@ApiModelProperty(
            value = "Network name on which it was connected.",
            example = "SMARTDUBAI-Device")
	@JsonProperty("networkName")
	private String networkName;
	
	@ApiModelProperty(
            value = "Device model of mobile.",
            example = "SM-G973F")
	@JsonProperty("deviceModel")
	private String deviceModel;

	@ApiModelProperty(
            value = "Device name of mobile.",
            example = "samsung")
	@JsonProperty("deviceName")
	private String deviceName;
	
	@ApiModelProperty(
            value = "Platform of the device.",
            example = "ANDROID")
	@JsonProperty("platform")
	private String platform;

	@ApiModelProperty(
            value = "Platform version of the device.",
            example = "29")
	@JsonProperty("platformVersion")
	private String platformVersion;
	
	@ApiModelProperty(
            value = "System version of the device.",
            example = "10")
	@JsonProperty("deviceSystemVersion")
	private String deviceSystemVersion;
	
	@ApiModelProperty(
            value = "Message displayed to user",
            example = "JSON parsing failed, invalid JSON object => json-failed")
	@JsonProperty("userMessage")
	public String userMessage;

	@ApiModelProperty(
            value = "Event name on which the this is published.",
            example = "Payment")
	@JsonProperty("event")
	private String event;

	@ApiModelProperty(
            value = "Time of the device in millis.",
            example = "1580379462908")
	@JsonProperty("deviceTime")
	private String deviceTime;

	@ApiModelProperty(
            value = "Check if the event has occured for error.",
            example = "true")
	@JsonProperty("error")
	private boolean isError;
	
	@ApiModelProperty(
            value = "Mobile error code.",
            example = "M1003")
	@JsonProperty("errorCode")
	public String errorCode;

	@ApiModelProperty(
            value = "Error object for tracking.",
            example = "Exception occured while payment is happening.")
	@JsonProperty("errorObject")
	public String errorObject;
	
	@ApiModelProperty(
            value = "Severity of the error.",
            example = "CRITICALs")
	@JsonProperty("errorSeverity")
	public String errorSeverity;

	@ApiModelProperty(
            value = "payload data",
            example = "{\"journey\": \"DPTrafficFines\",\"lang\": \"en\"}")
    @JsonProperty("payload")
    public String payload;

    @ApiModelProperty(
            value = "Status Code",
            example = "1")
    @JsonProperty("statusCode")
    public String statusCode;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(String deviceTime) {
		this.deviceTime = deviceTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

	public String getDeviceSystemVersion() {
		return deviceSystemVersion;
	}

	public void setDeviceSystemVersion(String deviceSystemVersion) {
		this.deviceSystemVersion = deviceSystemVersion;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorObject() {
		return errorObject;
	}

	public void setErrorObject(String errorObject) {
		this.errorObject = errorObject;
	}

	public String getErrorSeverity() {
		return errorSeverity;
	}

	public void setErrorSeverity(String errorSeverity) {
		this.errorSeverity = errorSeverity;
	}

	public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

	@Override
	public String toString() {
		try {
			return JsonUtil.toJson(this, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}