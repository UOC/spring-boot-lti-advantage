package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.elc.spring.lti.tool.registration.RegistrationService;
import edu.uoc.elc.spring.lti.security.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.lti.security.utils.TokenFactory;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xaracil@uoc.edu
 */
public class ToolFactory {

	HttpSessionOIDCLaunchSession oidcLaunchSession;

	public Tool from(RegistrationService registrationService, ToolDefinitionBean toolDefinitionBean, HttpServletRequest request) {
		return from(registrationService, toolDefinitionBean, request, false);
	}

	public Tool from(RegistrationService registrationService, ToolDefinitionBean toolDefinitionBean, String registrationId, HttpServletRequest request, boolean clearSession) {
		createSession(request, clearSession);
		createSession(request, clearSession);
		oidcLaunchSession.setRegistrationId(registrationId);
		return getTool(registrationService, toolDefinitionBean, request);
	}

	public Tool from(RegistrationService registrationService, ToolDefinitionBean toolDefinitionBean, HttpServletRequest request, boolean clearSession) {
		createSession(request, clearSession);
		return getTool(registrationService, toolDefinitionBean, request);
	}

	private void createSession(HttpServletRequest request, boolean clearSession) {
		oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		if (clearSession) {
			oidcLaunchSession.clear();
		}
	}

	@NotNull
	private Tool getTool(RegistrationService registrationService, ToolDefinitionBean toolDefinitionBean, HttpServletRequest request) {
		final Registration registration = registrationService.getRegistration(oidcLaunchSession.getRegistrationId());
		Tool tool = new Tool(registration,
						toolDefinitionBean.getClaimAccessor().getClaimAccessor(registration),
						oidcLaunchSession,
						toolDefinitionBean.getBuilders(registration));

		String token = TokenFactory.from(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
		return tool;
	}
}
