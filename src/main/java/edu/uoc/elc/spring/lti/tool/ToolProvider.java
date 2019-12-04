package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.platform.deeplinking.DeepLinkingClient;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.spring.lti.security.LTIAccessTokenProvider;
import edu.uoc.elc.spring.lti.ags.RestTemplateFactory;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class ToolProvider {
	private final Tool tool;
	private final RestTemplateFactory restTemplateFactory;

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
							tool.getAssignmentGradeServiceClientFactory(),
							restTemplateFactory);
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
