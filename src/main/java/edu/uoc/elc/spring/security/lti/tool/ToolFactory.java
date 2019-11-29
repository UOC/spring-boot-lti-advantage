package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.elc.spring.security.lti.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.security.lti.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolFactory {
	public Tool from(ToolDefinitionBean toolDefinitionBean, HttpServletRequest request) {
		final HttpSessionOIDCLaunchSession oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		final ToolDefinition toolDefinition = ToolDefinitionFactory.from(toolDefinitionBean);
		Tool tool = new Tool(toolDefinition,
						toolDefinitionBean.getClaimAccessor(),
						oidcLaunchSession,
						toolDefinitionBean.getBuilders());

		String token = RequestUtils.getToken(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
		return tool;
	}
}
