package edu.uoc.elearn.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * LTI Pre Auth filter. Tries to perform a preauth using LTI validation
 *
 * @author xaracil@uoc.edu
 */
public class LTIProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

	private ToolDefinition toolDefinition;

	public LTIProcessingFilter(ToolDefinition toolDefinition) {
		super();
		this.toolDefinition = toolDefinition;
		setAuthenticationDetailsSource(new LTIAuthenticationDetailsSource(toolDefinition));
	}

	public LTIProcessingFilter() {
		this(null);
	}

	private String getToken(HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getParameter("jwt");
		if (token == null || "".equals(token)) {
			token = httpServletRequest.getParameter("id_token");
		}
		return token;
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
		Tool tool = new Tool(toolDefinition.getName(), toolDefinition.getClientId(), toolDefinition.getKeySetUrl(), toolDefinition.getAccessTokenUrl(), toolDefinition.getPrivateKey(), toolDefinition.getPublicKey());
		String token = getToken(httpServletRequest);
		tool.validate(token);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Checking if request is a valid LTI");
		}
		if (tool.isValid()) {

			this.logger.info("Valid LTI call from " + tool.getUser().getId());
			return tool.getUser().getId() + "-" + tool.getContext().getId();
		}
		this.logger.info("The request is not a valid LTI one");
		return null;
	}

	// Store LTI context in credentials
	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		Tool tool = new Tool(toolDefinition.getName(), toolDefinition.getClientId(), toolDefinition.getKeySetUrl(), toolDefinition.getAccessTokenUrl(), toolDefinition.getPrivateKey(), toolDefinition.getPublicKey());
		String token = getToken(httpServletRequest);
		tool.validate(token);
		if (tool.isValid()) {
			return httpServletRequest;
		}

		return "{ N.A. }";
	}
}
