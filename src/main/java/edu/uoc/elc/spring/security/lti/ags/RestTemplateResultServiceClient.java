package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.elc.spring.security.lti.utils.QueryBuilder;
import edu.uoc.lti.ags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
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
		String query;
		try {
			query = QueryBuilder.of(new AbstractMap.SimpleImmutableEntry("limit", limit),
							new AbstractMap.SimpleImmutableEntry("page", page),
							new AbstractMap.SimpleImmutableEntry("userId", userId));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		String path = String.format( "%s/results", id);
		String uri = path + (!StringUtils.isEmpty(query) ? "?" + query : "");

		ResponseEntity<List<Result>> responseEntity = restTemplate.exchange(uri,
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<Result>>() {});
		return responseEntity.getBody();
	}
}
