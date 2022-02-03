package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.elc.lti.tool.ToolBuilders;
import edu.uoc.elc.spring.lti.tool.builders.ClaimAccessorService;
import edu.uoc.elc.spring.lti.tool.builders.ClientCredentialsTokenBuilderService;
import edu.uoc.elc.spring.lti.tool.builders.DeepLinkingTokenBuilderService;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xaracil@uoc.edu
 */
@Component
@RequiredArgsConstructor
public class ToolDefinitionBean {
	@Getter
	private final ClaimAccessorService claimAccessor;

	private final DeepLinkingTokenBuilderService deepLinkingTokenBuilder;
	private final ClientCredentialsTokenBuilderService clientCredentialsTokenBuilder;
	private final AccessTokenRequestBuilder accessTokenRequestBuilder;

	public ToolBuilders getBuilders(Registration registration) {
		return new ToolBuilders(clientCredentialsTokenBuilder.getClientCredentialsTokenBuilder(registration),
						accessTokenRequestBuilder,
						deepLinkingTokenBuilder.getDeepLinkingTokenBuilder(registration));
	}
}
