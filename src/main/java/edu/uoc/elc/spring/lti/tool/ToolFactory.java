package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.elc.spring.lti.security.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.lti.security.utils.TokenFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xaracil@uoc.edu
 */
public class ToolFactory {

	HttpSessionOIDCLaunchSession oidcLaunchSession;

	public Tool from(RegistrationService registrationService, ToolDefinitionBean toolDefinitionBean, HttpServletRequest request) {
		return from(registrationService, toolDefinitionBean, request, false);
	}

	public Tool from(RegistrationService registrationService, ToolDefinitionBean toolDefinitionBean, HttpServletRequest request, boolean clearSession) {
		oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		if (clearSession) {
			oidcLaunchSession.clear();
		}
		final Registration registration = registrationService.getRegistration(oidcLaunchSession.getRegistrationId());
		Tool tool = new Tool(registration,
						toolDefinitionBean.getClaimAccessor(),
						oidcLaunchSession,
						toolDefinitionBean.getBuilders());

		String token = TokenFactory.from(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
		return tool;
	}
}
