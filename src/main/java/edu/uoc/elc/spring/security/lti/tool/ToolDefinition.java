package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Component
@RequiredArgsConstructor
public class ToolDefinition {
	private final BasicDefinition basicDefinition;
	@Getter
	private final ClaimAccessor claimAccessor;
	@Getter
	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	@Getter
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;

	public String getName() {
		return basicDefinition.getName();
	}

	public String getClientId() {
		return basicDefinition.getClientId();
	}

	public String getKeySetUrl() {
		return basicDefinition.getKeySetUrl();
	}

	public String getPlatform() {
		return basicDefinition.getPlatform();
	}

	public String getAccessTokenUrl() {
		return basicDefinition.getAccessTokenUrl();
	}

	public String getOidcAuthUrl() {
		return basicDefinition.getOidcAuthUrl();
	}

	public String getPrivateKey() {
		return basicDefinition.getPrivateKey();
	}

	public String getPublicKey() {
		return basicDefinition.getPublicKey();
	}
}
