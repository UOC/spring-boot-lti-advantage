package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.elc.spring.security.lti.ags.converters.AgsMessageConverter;
import edu.uoc.lti.ags.ContentTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class RestTemplateLineItemServiceClientFactory {
	private final RestTemplateFactory restTemplateFactory;

	public RestTemplateLineItemServiceClient of(AccessTokenProvider accessTokenProvider) {
		return RestTemplateLineItemServiceClient.of(containerTemplate(accessTokenProvider), itemTemplate(accessTokenProvider));
	}

	private OAuth2RestOperations itemTemplate(AccessTokenProvider accessTokenProvider) {
		return restTemplateFactory.from(accessTokenProvider, new AgsMessageConverter(ContentTypes.LINE_ITEM));
	}

	private OAuth2RestOperations containerTemplate(AccessTokenProvider accessTokenProvider) {
		return restTemplateFactory.from(accessTokenProvider, new AgsMessageConverter(ContentTypes.LINE_ITEM_CONTAINER));
	}
}
