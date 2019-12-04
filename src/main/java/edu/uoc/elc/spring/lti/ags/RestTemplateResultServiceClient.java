package edu.uoc.elc.spring.lti.ags;

import edu.uoc.lti.ags.Result;
import edu.uoc.lti.ags.ResultServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor(staticName = "of")
public class RestTemplateResultServiceClient implements ResultServiceClient {
	private final OAuth2RestOperations restTemplate;

	/**
	 * Returns all the results for the line item (results for all the students in the current context for this line item) Results may be broken in multiple pages in particular if a limit parameter is present.
	 * @param id Id of the line item.
	 * @param limit restricts the maximum number of items to be returned. The response may be further constrained. If null doesn't apply this restriction.
	 * @param page indicates the offset for which this page should start containing items. Used exclusively by the nextPage URL. Can be null.
	 * @param userId limit the line items returned to only those which have been associated with this user. Results must contain at most one result.
	 * @return all the results for the line item
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Result> getLineItemResults(String id, Integer limit, Integer page, String userId) {
		String path = String.format( "%s/results", id);
		final MultiValueMap<String, String> query = queryWithParams(limit, page, userId);
		final String uri = UriComponentsBuilder.fromUriString(path).queryParams(query).build().toUriString();

		ResponseEntity<List<Result>> responseEntity = restTemplate.exchange(uri,
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<Result>>() {});
		return responseEntity.getBody();
	}

	private MultiValueMap<String, String> queryWithParams(Integer limit, Integer page, String userId) {
		MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
		query.add("limit", limit != null ? String.valueOf(limit) : null);
		query.add("page", limit != null ? String.valueOf(page) : null);
		query.add("userId", userId);
		return query;
	}
}
