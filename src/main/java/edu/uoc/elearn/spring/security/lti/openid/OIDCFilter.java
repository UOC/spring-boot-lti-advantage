package edu.uoc.elearn.spring.security.lti.openid;

import edu.uoc.elc.lti.tool.Tool;
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
	private final Tool tool;

	public OIDCFilter(String defaultFilterProcessesUrl, ToolDefinition toolDefinition) {
		super(defaultFilterProcessesUrl);
		this.tool = new Tool(toolDefinition.getName(), toolDefinition.getClientId(), toolDefinition.getKeySetUrl(), toolDefinition.getAccessTokenUrl(), toolDefinition.getOidcAuthUrl(), toolDefinition.getPrivateKey(), toolDefinition.getPublicKey());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		// get data from request
		final LoginRequest loginRequest = LoginRequestFactory.from(request);

		// verifications
		// TODO: verify issuers
		try {
			final URI uri = new URI(loginRequest.getTarget_link_uri());
			/* commented in local because localhost resolves to 0:0:0:0
			if (!uri.getHost().equals(request.getRemoteHost())) {
				throw new ServletException("Bad request");
			}
			*/
		} catch (URISyntaxException e) {
			throw new ServletException("Bad request");
		}

		// prepare redirect
		final LoginResponse oidcAuthParams = tool.getOidcAuthParams(loginRequest);

		// save state
		request.getSession().setAttribute("currentOidc", oidcAuthParams);

		// do the redirection
		String authRequest = tool.getOidcAuthUrl(oidcAuthParams);

		response.sendRedirect(authRequest);

		return null;
	}
}
