package edu.uoc.elearn.lti.provider.security;

import edu.uoc.elc.lti.tool.Tool;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * LTI Pre Auth filter. Tries to perform a preauth using LTI validation
 *
 * @author xaracil@uoc.edu
 */
public class LTIBasedPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

	private LTITool ltiTool;

	public LTIBasedPreAuthenticatedProcessingFilter(List<String> adminUsers, List<String> adminDomainCodes, LTITool ltiTool) {
		super();
		this.ltiTool = ltiTool;
		setAuthenticationDetailsSource(new LTIBasedPreAuthenticatedWebAuthenticationDetailsSource(adminUsers, adminDomainCodes, ltiTool));
	}

	public LTIBasedPreAuthenticatedProcessingFilter() {
		this(null, null, null);
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
		Tool tool = new Tool(ltiTool.getName(), ltiTool.getClientId(), ltiTool.getKeySetUrl(), ltiTool.getAccessTokenUrl(), ltiTool.getPrivateKey(), ltiTool.getPublicKey());
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
		Tool tool = new Tool(ltiTool.getName(), ltiTool.getClientId(), ltiTool.getKeySetUrl(), ltiTool.getAccessTokenUrl(), ltiTool.getPrivateKey(), ltiTool.getPublicKey());
		String token = getToken(httpServletRequest);
		tool.validate(token);
		if (tool.isValid()) {
			return httpServletRequest;
		}

		return "{ N.A. }";
	}
}
