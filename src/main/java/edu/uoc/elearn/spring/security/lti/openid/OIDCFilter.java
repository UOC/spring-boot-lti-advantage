package edu.uoc.elearn.spring.security.lti.openid;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.claims.JWSClaimAccessor;
import edu.uoc.elc.lti.tool.oidc.LoginRequest;
import edu.uoc.elc.lti.tool.oidc.LoginResponse;
import edu.uoc.elearn.spring.security.lti.tool.ToolDefinition;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class OIDCFilter extends AbstractAuthenticationProcessingFilter {
	private final ToolDefinition toolDefinition;

	public OIDCFilter(String defaultFilterProcessesUrl, ToolDefinition toolDefinition) {
		super(defaultFilterProcessesUrl);
		this.toolDefinition = toolDefinition;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		final HttpSessionOIDCLaunchSession oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		final JWSClaimAccessor jwsClaimAccessor = new JWSClaimAccessor(toolDefinition.getKeySetUrl());
		Tool tool = new Tool(toolDefinition.getName(),
						toolDefinition.getClientId(),
						toolDefinition.getPlatform(),
						toolDefinition.getKeySetUrl(),
						toolDefinition.getAccessTokenUrl(),
						toolDefinition.getOidcAuthUrl(),
						toolDefinition.getPrivateKey(),
						toolDefinition.getPublicKey(),
						jwsClaimAccessor,
						oidcLaunchSession);

		// get data from request
		final LoginRequest loginRequest = LoginRequestFactory.from(request);

		// verifications
		try {
			final URI uri = new URI(loginRequest.getTarget_link_uri());
			/* commented in local because localhost resolves to 0:0:0:0
			if (!uri.getHost().equals(request.getRemoteHost())) {
				throw new ServletException("Bad request");
			}
			*/

			// do the redirection
			String authRequest = tool.getOidcAuthUrl(loginRequest);

			response.sendRedirect(authRequest);

		} catch (URISyntaxException e) {
			throw new ServletException("Bad request");
		}

		return null;
	}
}
