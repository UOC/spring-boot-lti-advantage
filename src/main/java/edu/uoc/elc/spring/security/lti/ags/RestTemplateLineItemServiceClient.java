package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.lti.ags.ContentTypes;
import edu.uoc.lti.ags.LineItem;
import edu.uoc.lti.ags.LineItemServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor(staticName = "of")
public class RestTemplateLineItemServiceClient implements LineItemServiceClient {
	private final OAuth2RestOperations restContainerTemplate;
	private final OAuth2RestOperations restItemTemplate;
	@Setter
	private URI serviceUri;

	/**
	 * Get the line items of the platform
	 * @param limit restricts the maximum number of items to be returned. If null doesn't apply this restriction.
	 * @param page indicates the offset for which this page should start containing items. Can be null.
	 * @param resourceLinkId limit the line items returned to only those which have been associated with the specified tool platform's resource link. Can be null.
	 * @param tag limit the line items returned to only those which have been associated with the specified tag. Can be null.
	 * @param resourceId limit the line items returned to only those which have been associated with the specified tool resource ID. Can be null.
	 * @return all the line items associated to tool in the specified learning context. Results may be broken in multiple pages in particular if a limit parameter is present.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LineItem> getLineItems(Integer limit, Integer page, String resourceLinkId, String tag, String resourceId) {
		final MultiValueMap<String, String> query = queryWithParams(limit, page, resourceLinkId, tag, resourceId);
		final String uri = UriComponentsBuilder.fromUri(serviceUri).queryParams(query).build().toUriString();
		ResponseEntity<List<LineItem>> responseEntity = restContainerTemplate.exchange(uri,
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<LineItem>>() {});
		return responseEntity.getBody();
	}

	/**
	 * Add a new line item; the created line item is returned with the url, scores and results endpoints (assuming the client has the enabled capabilities for those)
	 * @param lineItem the LineItem to create
	 * @return the LineItem created
	 */
	@Override
	public LineItem createLineItem(LineItem lineItem) {
		HttpEntity<LineItem> lineItemHttpEntity = createHttEntity(lineItem);
		final ResponseEntity<LineItem> responseEntity = restItemTemplate.postForEntity(serviceUri, lineItemHttpEntity, LineItem.class);
		return responseEntity.getBody();
	}

	/**
	 * Returns the current value for the line item.
	 * @param id the id of the line item.
	 * @return the line item of the given id
	 */
	@Override
	public LineItem getLineItem(String id) {
		return restItemTemplate.getForObject(id, LineItem.class);
	}

	/**
	 * Updates the line item
	 * @param id Id of the line item.
	 * @param lineItem the updated line item.
	 * @return the updated line item.
	 */
	@Override
	public LineItem updateLineItem(String id, LineItem lineItem) {
		HttpEntity<LineItem> lineItemHttpEntity = createHttEntity(lineItem);
		ResponseEntity<LineItem> responseEntity = restItemTemplate.exchange(id,
						HttpMethod.PUT,
						lineItemHttpEntity,
						new ParameterizedTypeReference<LineItem>() {});

		return responseEntity.getBody();
	}

	/**
	 * Removes the line item. While no more associated with the tool, the tool platform may decide to keep the line item around (unassociated)
	 * @param id Id of the line item.
	 */
	@Override
	public void deleteLineItem(String id) {
		restItemTemplate.delete(id);
	}

	@NotNull
	private MultiValueMap<String, String> queryWithParams(Integer limit, Integer page, String resourceLinkId, String tag, String resourceId) {
		MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
		query.add("limit", limit != null ? String.valueOf(limit) : null);
		query.add("page", limit != null ? String.valueOf(page) : null);
		query.add("resourceLinkId", resourceLinkId);
		query.add("tag", tag);
		query.add("resourceId", resourceId);
		return query;
	}

	private HttpEntity<LineItem> createHttEntity(LineItem lineItem) {
		MediaType contentType = new MediaType(ContentTypes.LINE_ITEM.getType(), ContentTypes.LINE_ITEM.getSubtype(), StandardCharsets.UTF_8);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(contentType);
		return new HttpEntity<>(lineItem, httpHeaders);
	}

}
