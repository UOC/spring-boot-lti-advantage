package edu.uoc.elc.spring.security.lti.openid;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.oidc.LoginRequest;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinition;
import edu.uoc.elc.spring.security.lti.tool.ToolFactory;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
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
		ToolFactory toolFactory = new ToolFactory();
		final Tool tool = toolFactory.from(toolDefinition, request);

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
