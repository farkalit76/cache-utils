package ae.gov.sdg.paperless.platform.exceptions;

import java.io.Serializable;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import ae.gov.sdg.paperless.platform.common.model.IJourneyType;
import ae.gov.sdg.paperless.platform.common.model.components.types.LangType;


/**
 * Exception context for handling generic error template.
 * 
 * @author c_chandra.bommise
 *
 */
public class GenericExceptionContext implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Object request;
	private final String filename;
	private Map<String, Object> dictionary;
	private final LangType lang;
	private final String processId;
	private final HttpHeaders headers;
	private Severity severity = Severity.MEDIUM;
	private IJourneyType journey;

	public Severity getSeverity() {
		return severity;
	}

	public GenericExceptionContext(final Object request, final String filename, final Map<String, Object> dictionary, final LangType lang,
			final String processId, final HttpHeaders headers) {
		super();
		this.request = request;
		this.filename = filename;
		this.dictionary = dictionary;
		this.lang = lang == null ? LangType.en : lang;
		this.processId = processId;
		this.headers = headers;
	}

	public GenericExceptionContext(final Object request, final String filename, final Map<String, Object> dictionary, final LangType lang,
			final String processId, final HttpHeaders headers, final Severity severity) {
		super();
		this.request = request;
		this.filename = filename;
		this.dictionary = dictionary;
		this.lang = lang == null ? LangType.en : lang;
		this.processId = processId;
		this.headers = headers;
		this.severity=severity;
	}
	
	public GenericExceptionContext(final Object request, final String filename, final Map<String, Object> dictionary, final LangType lang,
			final String processId, final HttpHeaders headers, final IJourneyType journey) {
		super();
		this.request = request;
		this.filename = filename;
		this.dictionary = dictionary;
		this.lang = lang == null ? LangType.en : lang;
		this.processId = processId;
		this.headers = headers;
		this.journey = journey;
	}
	
	public GenericExceptionContext(final Object request, final String filename, final Map<String, Object> dictionary, final LangType lang,
			final String processId, final HttpHeaders headers,final Severity severity, final IJourneyType journey) {
		super();
		this.request = request;
		this.filename = filename;
		this.dictionary = dictionary;
		this.lang = lang == null ? LangType.en : lang;
		this.processId = processId;
		this.headers = headers;
		this.severity=severity;
		this.journey = journey;
	}

	public Object getRequest() {
		return request;
	}

	public String getFilename() {
		return filename;
	}

	public Map<String, Object> getDictionary() {
		return dictionary;
	}

	public LangType getLang() {
		return lang;
	}

	public String getProcessId() {
		return processId;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}
	public void setDictionary(final Map<String, Object> dictionary) {
		this.dictionary = dictionary;
	}
	public IJourneyType getJourney() {
		return journey;
	}
}