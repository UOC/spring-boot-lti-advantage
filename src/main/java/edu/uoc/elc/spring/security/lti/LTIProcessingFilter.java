package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinition;
import edu.uoc.elc.spring.security.lti.tool.ToolFactory;
import edu.uoc.elc.spring.security.lti.utils.RequestUtils;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * LTI Pre Auth filter. Tries to perform a preauth using LTI validation
 *
 * @author xaracil@uoc.edu
 */
public class LTIProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

	private final ToolDefinition toolDefinition;
	private final ClaimAccessor claimAccessor;
	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;

	private Tool tool;

	public LTIProcessingFilter(ToolDefinition toolDefinition, ClaimAccessor claimAccessor, DeepLinkingTokenBuilder deepLinkingTokenBuilder, ClientCredentialsTokenBuilder clientCredentialsTokenBuilder) {
		super();
		this.toolDefinition = toolDefinition;
		this.claimAccessor = claimAccessor;
		this.deepLinkingTokenBuilder = deepLinkingTokenBuilder;
		this.clientCredentialsTokenBuilder = clientCredentialsTokenBuilder;
		setAuthenticationDetailsSource(new LTIAuthenticationDetailsSource(toolDefinition, claimAccessor, deepLinkingTokenBuilder, clientCredentialsTokenBuilder));
	}

	private Tool getTool(HttpServletRequest httpServletRequest) {
		if (tool == null) {
			ToolFactory toolFactory = new ToolFactory();
			this.tool = toolFactory.from(toolDefinition, claimAccessor, deepLinkingTokenBuilder, clientCredentialsTokenBuilder, httpServletRequest);
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

		getTool(httpServletRequest).validate(token, state);

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
		String token = RequestUtils.getToken(httpServletRequest);
		String state = httpServletRequest.getParameter("state");

		getTool(httpServletRequest).validate(token, state);
		if (tool.isValid()) {
			return httpServletRequest;
		}

		return "{ N.A. }";
	}

	private String getStateFromSession(HttpServletRequest httpServletRequest) {
		return (String) httpServletRequest.getSession().getAttribute("currentLti1.3State");
	}

	private boolean stateIsValid(String state, String sessionState) {
		if (state == null && sessionState == null) {
			return true;
		}
		if (state != null) {
			return state.equals(sessionState);
		}
		return false;
	}
}