package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.security.lti.utils.RequestUtils;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.jwt.claims.JWSClaimAccessor;
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder;
import edu.uoc.lti.jwt.deeplink.JWSTokenBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolFactory {
	public Tool from(ToolDefinition toolDefinition, HttpServletRequest request) {
		final HttpSessionOIDCLaunchSession oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		final JWSClaimAccessor jwsClaimAccessor = new JWSClaimAccessor(toolDefinition.getKeySetUrl());
		final DeepLinkingTokenBuilder deepLinkingTokenBuilder = new JWSTokenBuilder(toolDefinition.getPublicKey(), toolDefinition.getPrivateKey());
		final JWSClientCredentialsTokenBuilder clientCredentialsTokenBuilder = new JWSClientCredentialsTokenBuilder(toolDefinition.getPublicKey(), toolDefinition.getPrivateKey());
		Tool tool = new Tool(toolDefinition.getName(),
						toolDefinition.getClientId(),
						toolDefinition.getPlatform(),
						toolDefinition.getKeySetUrl(),
						toolDefinition.getAccessTokenUrl(),
						toolDefinition.getOidcAuthUrl(),
						toolDefinition.getPrivateKey(),
						toolDefinition.getPublicKey(),
						jwsClaimAccessor,
						oidcLaunchSession,
						deepLinkingTokenBuilder,
						clientCredentialsTokenBuilder);

		String token = RequestUtils.getToken(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
		return tool;
	}
}
