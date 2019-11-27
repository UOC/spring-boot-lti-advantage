package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinition;
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

	private final ToolDefinition toolDefinition;

	private Tool tool;

	public LTIProcessingFilter(ToolDefinition toolDefinition) {
		super();
		this.toolDefinition = toolDefinition;
		setAuthenticationDetailsSource(new LTIAuthenticationDetailsSource(toolDefinition));
	}

	private Tool getTool(HttpServletRequest httpServletRequest) {
		if (tool == null) {
			ToolFactory toolFactory = new ToolFactory();
			this.tool = toolFactory.from(toolDefinition, httpServletRequest);
		}
		return this.tool;
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

		getTool(httpServletRequest).validate(token, state);
		if (tool.isValid()) {
			this.logger.info("Valid LTI call from " + tool.getUser().getId());
			return tool.getUser().getId() + (tool.getContext() != null ? "-" + tool.getContext().getId() : "");
		}
		this.logger.info("The request is not a valid LTI one. Reason: " + tool.getReason());
		return null;
	}

	// Store LTI context in credentials
	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		String token = RequestUtils.getToken(httpServletRequest);
		String state = httpServletRequest.getParameter("state");

		if (token == null) {
			this.logger.info("The request is not a LTI request, so no credentials at all. Returning current credentials");
			Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
			return currentUser != null ? currentUser.getCredentials() : null;
		}

		getTool(httpServletRequest).validate(token, state);
		if (tool.isValid()) {
			return tool;
		}

		return "{ N.A. }";
	}
}
