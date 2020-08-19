package ae.gov.sdg.paperless.platform.directory.service;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.REQUIRED_PAGNIATION_INFO_NOT_PRESENT;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ae.gov.sdg.paperless.platform.common.model.UserInfo;
import ae.gov.sdg.paperless.platform.common.service.generic.BaseJourneyService;
import ae.gov.sdg.paperless.platform.directory.component.DirectoryComponentResponse;
import ae.gov.sdg.paperless.platform.directory.component.DirectoryRequest;


/**
 * The Class AbstractDirectoryService.
 * 
 * @author c_vijendra.singh
 */
public abstract class AbstractDirectoryService extends BaseJourneyService implements IDirectoryService {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDirectoryService.class);
	public static final String PAGE_NUM = "pageNum";
	public static final String ROW_SIZE = "rowSize";
	
	/**
	 * Listing.
	 *
	 * @param request the request
	 * @param userInfo the user info
	 * @param dubaiNowToken the dubai now token
	 * @return the directory component response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public DirectoryComponentResponse listing(DirectoryRequest request, UserInfo userInfo, String dubaiNowToken)
			throws IOException {
		LOG.info("Fetch the data: {}", request);
		ResponseEntity<String> response = null;
		response = processRequest(request, userInfo, dubaiNowToken);
		if (response == null || response.getBody() == null) {
			return null;
		}
		return transform(response.getBody(), request);
	}

	/**
	 * Process request.
	 *
	 * @param request the request
	 * @param userInfo the user info
	 * @param dubaiNowToken the dubai now token
	 * @return the response entity
	 * @throws JsonProcessingException the json processing exception
	 */
	protected abstract ResponseEntity<String> processRequest(DirectoryRequest request, UserInfo userInfo, String dubaiNowToken) throws JsonProcessingException;

	/**
	 * Transform.
	 *
	 * @param json the json
	 * @param request the request
	 * @return the directory component response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract DirectoryComponentResponse transform(String json, DirectoryRequest request) throws IOException;

	/**
	 *  Build the pagination map based on values.
	 *
	 * @param recordIndex
	 * @param recordIndex
	 * @param recordIndex
	 * @param recordIndex
	 * @param limit
	 * @return
	 */
	protected MultiValueMap<String, String> buildRequestPaginationMap(Integer recordIndex, Integer requestLimit) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		Integer limit = null;
		Integer startPage = null;
		Validate.noNullElements(Arrays.asList(requestLimit, recordIndex),  REQUIRED_PAGNIATION_INFO_NOT_PRESENT);
		limit = requestLimit!=null? requestLimit: limit;
		startPage = recordIndex!=null? recordIndex: startPage;

		map.add(PAGE_NUM, String.valueOf(startPage));
		map.add(ROW_SIZE, String.valueOf(limit));

		return map;
	}

	/**
	 * List of objects from JSON
	 *
	 * @param <T>
	 * @param json
	 * @param typeReference
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> fromJson(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
		return mapper.readValue(json, new TypeReference<List<T>>() {});
	}

}