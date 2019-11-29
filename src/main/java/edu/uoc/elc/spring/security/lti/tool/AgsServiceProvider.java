package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.platform.ags.AgsClientFactory;
import edu.uoc.elc.lti.platform.ags.GenericResultServiceClient;
import edu.uoc.elc.lti.platform.ags.GenericScoreServiceClient;
import edu.uoc.elc.lti.platform.ags.ToolLineItemServiceClient;
import edu.uoc.elc.spring.security.lti.ags.RestTemplateFactory;
import edu.uoc.elc.spring.security.lti.ags.RestTemplateLineItemServiceClientFactory;
import edu.uoc.elc.spring.security.lti.ags.RestTemplateResultServiceClientFactory;
import edu.uoc.elc.spring.security.lti.ags.RestTemplateScoreServiceClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class AgsServiceProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final AgsClientFactory clientFactory;
	private final RestTemplateFactory restTemplateFactory;

	private OAuth2RestOperations containerTemplate;
	private OAuth2RestOperations intemTemplate;

	public ToolLineItemServiceClient getLineItemsServiceClient() {
		RestTemplateLineItemServiceClientFactory factory = new RestTemplateLineItemServiceClientFactory(restTemplateFactory);
		return clientFactory.getLineItemServiceClient(
						factory.of(accessTokenProvider)
		);
	}

	public GenericResultServiceClient getResultsServiceClient () {
		RestTemplateResultServiceClientFactory factory = new RestTemplateResultServiceClientFactory(restTemplateFactory);
		return clientFactory.getResultServiceClient(factory.of(accessTokenProvider));
	}

	public GenericScoreServiceClient getScoreServiceClient () {
		RestTemplateScoreServiceClientFactory factory = new RestTemplateScoreServiceClientFactory(restTemplateFactory);
		return clientFactory.getScoreServiceClient(factory.of(accessTokenProvider));
	}
}
