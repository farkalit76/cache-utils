package ae.gov.sdg.paperless.platform.exceptions;

import java.time.LocalDateTime;

public class ApiError {

	private final LocalDateTime timestamp = LocalDateTime.now();
	private Integer status;
	private String error;
	private String message;
    private String path;
    private String errorCode;
    private String errorSeverity;

    private ApiError() {
    }

    public ApiError(final String errorCode) {
        this();
        this.errorCode = errorCode;
    }

    public ApiError(final String errorCode, final String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    public ApiError(final Integer status, final String error, final String message, final String path, final String errorCode,
			final String errorSeverity) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.errorCode = errorCode;
		this.errorSeverity = errorSeverity;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(final Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(final String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorSeverity() {
		return errorSeverity;
	}

	public void setErrorSeverity(final String errorSeverity) {
		this.errorSeverity = errorSeverity;
	}

}

