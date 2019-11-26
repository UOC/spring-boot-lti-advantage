package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.security.lti.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolFactory {
	public Tool from(ToolDefinition toolDefinition, HttpServletRequest request) {
		final HttpSessionOIDCLaunchSession oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		Tool tool = new Tool(toolDefinition.getName(),
						toolDefinition.getClientId(),
						toolDefinition.getPlatform(),
						toolDefinition.getDeploymentId(),
						toolDefinition.getKeySetUrl(),
						toolDefinition.getAccessTokenUrl(),
						toolDefinition.getOidcAuthUrl(),
						toolDefinition.getPrivateKey(),
						toolDefinition.getPublicKey(),
						toolDefinition.getClaimAccessor(),
						oidcLaunchSession,
						toolDefinition.getDeepLinkingTokenBuilder(),
						toolDefinition.getClientCredentialsTokenBuilder());

		String token = RequestUtils.getToken(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
		return tool;
	}
}
