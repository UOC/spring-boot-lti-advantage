package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.platform.ags.AgsClientFactory;
import edu.uoc.elc.lti.platform.ags.GenericResultServiceClient;
import edu.uoc.elc.lti.platform.ags.GenericScoreServiceClient;
import edu.uoc.elc.lti.platform.ags.ToolLineItemServiceClient;
import edu.uoc.elc.spring.lti.ags.RestTemplateFactory;
import edu.uoc.elc.spring.lti.ags.RestTemplateLineItemServiceClientFactory;
import edu.uoc.elc.spring.lti.ags.RestTemplateResultServiceClientFactory;
import edu.uoc.elc.spring.lti.ags.RestTemplateScoreServiceClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class AgsServiceProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final AgsClientFactory clientFactory;
	private final RestTemplateFactory restTemplateFactory;

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

	public boolean hasAgsService() {
		return clientFactory.hasAgsService();
	}
}
