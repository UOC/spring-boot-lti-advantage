package edu.uoc.elc.spring.lti.security;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.lti.security.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.lti.tool.ToolDefinitionBean;
import edu.uoc.elc.spring.lti.tool.ToolFactory;
import edu.uoc.elc.spring.lti.security.utils.TokenFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

/**
 * LTI Pre Auth filter. Tries to perform a preauth using LTI validation
 *
 * @author xaracil@uoc.edu
 */
public class LTIProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

	private final ToolDefinitionBean toolDefinitionBean;
	private final boolean invalidateSession;

	public LTIProcessingFilter(ToolDefinitionBean toolDefinitionBean) {
		super();
		this.setRequiresAuthenticationRequestMatcher(new LTIProcessingFilter.LTIPreAuthenticatedProcesssingRequestMatcher());
		this.setInvalidateSessionOnPrincipalChange(false); // set to false because we'll remove OIDC attributes stored in session otherwise
		this.invalidateSession = true;
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

		String token = TokenFactory.from(httpServletRequest);
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
			return token;
		}
		this.logger.info("The request is not a valid LTI one. Reason: " + tool.getReason());
		return null;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		String token = TokenFactory.from(httpServletRequest);
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

	private class LTIPreAuthenticatedProcesssingRequestMatcher implements RequestMatcher {
		public LTIPreAuthenticatedProcesssingRequestMatcher() {
		}

		public boolean matches(HttpServletRequest request) {
			Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
			if (currentUser == null) {
				return true;
			} else if (!LTIProcessingFilter.this.principalChanged(request, currentUser)) {
				return false;
			} else {
				LTIProcessingFilter.this.logger.debug("Pre-authenticated principal has changed and will be reauthenticated");


				if (LTIProcessingFilter.this.invalidateSession) {
					SecurityContextHolder.clearContext();
					HttpSession session = request.getSession(false);
					if (session != null) {
						LTIProcessingFilter.this.logger.debug("Invalidating existing session");
						final List<String> keysToSave = HttpSessionOIDCLaunchSession.KEYS;
						final Enumeration<String> attributeNames = session.getAttributeNames();
						while (attributeNames.hasMoreElements()) {
							final String name = attributeNames.nextElement();
							if (!keysToSave.contains(name)) {
								session.removeAttribute(name);
							}
						}
					}
				}

				return true;
			}
		}
	}
}
