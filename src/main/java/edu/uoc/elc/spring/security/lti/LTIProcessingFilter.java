package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinitionBean;
import edu.uoc.elc.spring.security.lti.tool.ToolFactory;
import edu.uoc.elc.spring.security.lti.utils.RequestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * LTI Pre Auth filter. Tries to perform a preauth using LTI validation
 *
 * @author xaracil@uoc.edu
 */
public class LTIProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

	private final ToolDefinitionBean toolDefinitionBean;

	public LTIProcessingFilter(ToolDefinitionBean toolDefinitionBean) {
		super();
		this.toolDefinitionBean = toolDefinitionBean;
		setAuthenticationDetailsSource(new LTIAuthenticationDetailsSource(toolDefinitionBean));
	}

	private Tool getTool(HttpServletRequest httpServletRequest) {
		ToolFactory toolFactory = new ToolFactory();
		return toolFactory.from(toolDefinitionBean, httpServletRequest);
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Checking if request is a valid LTI");
		}

		String token = RequestUtils.getToken(httpServletRequest);
		String state = httpServletRequest.getParameter("state");

		if (token == null) {
			this.logger.info("The request is not a LTI request, so no principal at all. Returning current principal");
			Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
			return currentUser != null ? currentUser.getName() : null;
		}

		final Tool tool = getTool(httpServletRequest);
		tool.validate(token, state);
		if (tool.isValid()) {
			this.logger.info("Valid LTI call from " + tool.getUser().getId());
			return tool.getUser().getId() + (tool.getContext() != null ? "-" + tool.getContext().getId() : "");
		}
		this.logger.info("The request is not a valid LTI one. Reason: " + tool.getReason());
		return null;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		String token = RequestUtils.getToken(httpServletRequest);
		String state = httpServletRequest.getParameter("state");

		if (token == null) {
			this.logger.info("The request is not a LTI request, so no credentials at all. Returning current credentials");
			Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
			return currentUser != null ? currentUser.getCredentials() : null;
		}

		final Tool tool = getTool(httpServletRequest);
		tool.validate(token, state);
		if (tool.isValid()) {
			return tool;
		}

		return "{ N.A. }";
	}
}
