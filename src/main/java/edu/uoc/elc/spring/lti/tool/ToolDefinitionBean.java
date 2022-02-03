package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.ToolBuilders;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@Component
@RequiredArgsConstructor
public class ToolDefinitionBean {
	@Getter
	private final ClaimAccessor claimAccessor;

	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;
	private final AccessTokenRequestBuilder accessTokenRequestBuilder;

	public ToolBuilders getBuilders() {
		return new ToolBuilders(clientCredentialsTokenBuilder, accessTokenRequestBuilder, deepLinkingTokenBuilder);
	}
}
