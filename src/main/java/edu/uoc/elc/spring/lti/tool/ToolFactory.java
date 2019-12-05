package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.elc.spring.lti.security.openid.HttpSessionOIDCLaunchSession;
import edu.uoc.elc.spring.lti.security.utils.TokenFactory;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xaracil@uoc.edu
 */
public class ToolFactory {
	@Getter
	private HttpSessionOIDCLaunchSession oidcLaunchSession;

	public Tool from(ToolDefinitionBean toolDefinitionBean, HttpServletRequest request) {
		this.oidcLaunchSession = new HttpSessionOIDCLaunchSession(request);
		final ToolDefinition toolDefinition = ToolDefinitionFactory.from(toolDefinitionBean);
		Tool tool = new Tool(toolDefinition,
						toolDefinitionBean.getClaimAccessor(),
						oidcLaunchSession,
						toolDefinitionBean.getBuilders());

		String token = TokenFactory.from(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
		return tool;
	}
}
