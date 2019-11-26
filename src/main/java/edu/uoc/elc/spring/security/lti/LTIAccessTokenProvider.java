package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.lti.platform.accesstoken.AccessTokenResponse;
import edu.uoc.elc.lti.tool.Tool;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@AllArgsConstructor
public class LTIAccessTokenProvider implements AccessTokenProvider {
	private Tool tool;
	private final static long MINUTES = 60 * 1000;

	@Override
	public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, AccessTokenRequest accessTokenRequest) throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException {
		try {
			final AccessTokenResponse accessTokenResponse = tool.getAccessToken();
			final DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(accessTokenResponse.getAccessToken());
			accessToken.setTokenType(accessTokenResponse.getTokenType());
			accessToken.setExpiration(new Date(System.currentTimeMillis() + accessTokenResponse.getExpiresIn() * MINUTES));

			// scopes
			final String scope = accessTokenResponse.getScope();
			if (!StringUtils.isEmpty(scope)) {
				String[] scopeArray = scope.split(" ");
				accessToken.setScope(new HashSet<>(Arrays.asList(scopeArray)));
			}

			return accessToken;
		} catch (IOException e) {
			throw new AccessDeniedException("Couldn't obtain the access token", e);
		}
	}

	@Override
	public boolean supportsResource(OAuth2ProtectedResourceDetails resource) {
		return resource instanceof ClientCredentialsResourceDetails && "client_credentials".equals(resource.getGrantType());
	}

	@Override
	public OAuth2AccessToken refreshAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, OAuth2RefreshToken oAuth2RefreshToken, AccessTokenRequest accessTokenRequest) throws UserRedirectRequiredException {
		return null;
	}

	@Override
	public boolean supportsRefresh(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
		return false;
	}
}
