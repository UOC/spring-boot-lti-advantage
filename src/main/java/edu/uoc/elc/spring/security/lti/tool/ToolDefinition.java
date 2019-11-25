package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Component
@RequiredArgsConstructor
public class ToolDefinition {
	private final BasicToolDefinition basicToolDefinition;
	@Getter
	private final ClaimAccessor claimAccessor;
	@Getter
	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	@Getter
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;

	public String getName() {
		return basicToolDefinition.getName();
	}

	public String getClientId() {
		return basicToolDefinition.getClientId();
	}

	public String getKeySetUrl() {
		return basicToolDefinition.getKeySetUrl();
	}

	public String getPlatform() {
		return basicToolDefinition.getPlatform();
	}

	public String getAccessTokenUrl() {
		return basicToolDefinition.getAccessTokenUrl();
	}

	public String getOidcAuthUrl() {
		return basicToolDefinition.getOidcAuthUrl();
	}

	public String getPrivateKey() {
		return basicToolDefinition.getPrivateKey();
	}

	public String getPublicKey() {
		return basicToolDefinition.getPublicKey();
	}
}
