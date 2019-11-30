package edu.uoc.elc.spring.lti.ags;

import edu.uoc.elc.spring.lti.ags.converters.AgsMessageConverter;
import edu.uoc.lti.ags.ContentTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class RestTemplateScoreServiceClientFactory {
	private final RestTemplateFactory restTemplateFactory;

	public RestTemplateScoreServiceClient of(AccessTokenProvider accessTokenProvider) {
		return RestTemplateScoreServiceClient.of(restTemplate(accessTokenProvider));
	}

	private OAuth2RestOperations restTemplate(AccessTokenProvider accessTokenProvider) {
		return restTemplateFactory.from(accessTokenProvider, new AgsMessageConverter(ContentTypes.SCORE));
	}
}
