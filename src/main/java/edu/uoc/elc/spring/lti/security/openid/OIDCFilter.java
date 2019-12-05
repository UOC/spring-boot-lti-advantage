package edu.uoc.elc.spring.lti.security.openid;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.oidc.LoginRequest;
import edu.uoc.elc.spring.lti.tool.ToolDefinitionBean;
import edu.uoc.elc.spring.lti.tool.ToolFactory;
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
 * @author xaracil@uoc.edu
 */
public class OIDCFilter extends AbstractAuthenticationProcessingFilter {
	private final ToolDefinitionBean toolDefinitionBean;

	public OIDCFilter(String defaultFilterProcessesUrl, ToolDefinitionBean toolDefinitionBean) {
		super(defaultFilterProcessesUrl);
		this.toolDefinitionBean = toolDefinitionBean;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		ToolFactory toolFactory = new ToolFactory();
		toolFactory.getOidcLaunchSession().clear();
		final Tool tool = toolFactory.from(toolDefinitionBean, request);

		// get data from request
		final LoginRequest loginRequest = LoginRequestFactory.from(request);
		if (this.logger.isInfoEnabled()) {
			this.logger.info("OIDC launch received with " + loginRequest.toString());
		}

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
