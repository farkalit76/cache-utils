package ae.gov.sdg.paperless.platform.exceptions;

import java.util.Map;

import org.springframework.http.HttpHeaders;

import ae.gov.sdg.paperless.platform.common.model.IJourneyType;
import ae.gov.sdg.paperless.platform.common.model.components.types.LangType;

/**
 * Exception context for handling generic error template.
 *
 * @author c_chandra.bommise
 */
public class BusinessValidationExceptionContext extends GenericExceptionContext {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessValidationExceptionContext(final Object request, final String screenName, final Map<String, Object> dictionary, final LangType lang,
                                              final String processId, final HttpHeaders headers, final IJourneyType journey) {
        this(request, screenName, dictionary, lang, processId, headers, Severity.LOW, journey);
    }

    public BusinessValidationExceptionContext(final Object request, final String screenName, final Map<String, Object> dictionary, final LangType lang,
                                              final String processId, final HttpHeaders headers, final Severity severity, final IJourneyType journey) {
        super(request, screenName, dictionary, lang == null ? LangType.en : lang, processId, headers, severity, journey);
    }

    public BusinessValidationExceptionContext(final Object request, final String screenName, final Map<String, Object> dictionary, final LangType lang, final Severity severity, final IJourneyType journey) {
        this(request, screenName, dictionary, lang, null, null, severity, journey);
    }

    public BusinessValidationExceptionContext(final String screenName, final Map<String, Object> dictionary, final LangType lang, final Severity severity, final IJourneyType journey) {
        this(null, screenName, dictionary, lang, "", null, severity, journey);
    }

    public BusinessValidationExceptionContext(final String screenName, final Map<String, Object> dictionary, final LangType lang, final IJourneyType journey) {
        this(null, screenName, dictionary, lang, "", null, Severity.LOW, journey);
    }

    public BusinessValidationExceptionContext(final Object request, final String screenName, final Map<String, Object> dictionary, final LangType lang,
            final String processId, final HttpHeaders headers, final Severity severity) {
        super(null, screenName, dictionary, lang, "", null, Severity.LOW);
    }
    
	public BusinessValidationExceptionContext(final Object request, final String screenName, final Map<String, Object> dictionary,
			final LangType lang, final String processId, final HttpHeaders headers) {
		this(request, screenName, dictionary, lang, processId, headers, Severity.LOW);
	}

	public BusinessValidationExceptionContext(final Object request, final String screenName, final Map<String, Object> dictionary,
			final LangType lang, final Severity severity) {
		this(request, screenName, dictionary, lang, null, null, severity);
	}

	public BusinessValidationExceptionContext(final String screenName, final Map<String, Object> dictionary, final LangType lang,
			final Severity severity) {
		this(null, screenName, dictionary, lang, "", null, severity);
	}

	public BusinessValidationExceptionContext(final String screenName, final Map<String, Object> dictionary, final LangType lang) {
		this(null, screenName, dictionary, lang, "", null, Severity.LOW);
	}
    
}