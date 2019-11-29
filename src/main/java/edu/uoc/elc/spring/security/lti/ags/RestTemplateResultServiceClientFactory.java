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
public class RestTemplateResultServiceClientFactory {
	private final RestTemplateFactory restTemplateFactory;

	public RestTemplateResultServiceClient of(AccessTokenProvider accessTokenProvider) {
		return RestTemplateResultServiceClient.of(restTemplate(accessTokenProvider));
	}

	private OAuth2RestOperations restTemplate(AccessTokenProvider accessTokenProvider) {
		return restTemplateFactory.from(accessTokenProvider, new AgsMessageConverter(ContentTypes.RESULT));
	}
}
