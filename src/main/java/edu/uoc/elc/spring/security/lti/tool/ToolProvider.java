package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.platform.deeplinking.DeepLinkingClient;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.spring.security.lti.LTIAccessTokenProvider;
import lombok.RequiredArgsConstructor;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class ToolProvider {
	private final Tool tool;

	private NamesRoleServiceProvider namesRoleServiceProvider;
	private AgsServiceProvider agsServiceProvider;

	private LTIAccessTokenProvider ltiAccessTokenProvider;

	public Settings getDeepLinkingSettings() {
		return tool.getDeepLinkingSettings();
	}

	public DeepLinkingClient getDeepLinkingClient() {
		return tool.getDeepLinkingClient();
	}

	public NamesRoleServiceProvider getNamesRoleServiceProvider() {
		if (namesRoleServiceProvider == null) {
			namesRoleServiceProvider = new NamesRoleServiceProvider(getLTIAccessTokenProvider(),
							tool.getNameRoleService(),
							tool.getResourceLink());
		}
		return namesRoleServiceProvider;
	}

	public AgsServiceProvider getAgsServiceProvider() {
		if (agsServiceProvider == null) {
			agsServiceProvider = new AgsServiceProvider(getLTIAccessTokenProvider(),
							tool.getAssignmentGradeService());
		}
		return agsServiceProvider;
	}

	private LTIAccessTokenProvider getLTIAccessTokenProvider() {
		if (ltiAccessTokenProvider == null) {
			ltiAccessTokenProvider = new LTIAccessTokenProvider(tool);
		}
		return ltiAccessTokenProvider;
	}


}
