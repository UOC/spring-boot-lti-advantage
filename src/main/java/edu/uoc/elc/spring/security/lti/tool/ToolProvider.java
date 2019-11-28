package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elc.lti.platform.NamesRoleServiceResponse;
import edu.uoc.elc.lti.platform.deeplinking.DeepLinkingClient;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.spring.security.lti.LTIAccessTokenProvider;
import edu.uoc.elc.spring.security.lti.ags.AgsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.List;
import java.util.Objects;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class ToolProvider {
	private final Tool tool;

	private OAuth2RestOperations template;

	private NamesRoleServiceProvider namesRoleServiceProvider;

	public Settings getDeepLinkingSettings() {
		return tool.getDeepLinkingSettings();
	}

	public DeepLinkingClient getDeepLinkingClient() {
		return tool.getDeepLinkingClient();
	}

	public NamesRoleServiceProvider getNamesRoleServiceProvider() {
		if (namesRoleServiceProvider == null) {
			namesRoleServiceProvider = new NamesRoleServiceProvider(new LTIAccessTokenProvider(tool),
							tool.getNameRoleService(),
							tool.getResourceLink());
		}
		return namesRoleServiceProvider;
	}

	/**
	 * Return a client for platform's Assignment and Grade Service
	 * @return client for platform's Assignment and Grade Service
	 */
	public AgsClient getAssignmentAndGradeService() {
		return AgsClient.of(getTemplate(), tool.getAssignmentGradeService());
	}

	private OAuth2RestOperations getTemplate() {
		if (template == null) {
			final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
			OAuth2ClientContext context = new DefaultOAuth2ClientContext();

			final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
			oAuth2RestTemplate.setAccessTokenProvider(new LTIAccessTokenProvider(tool));
			template = oAuth2RestTemplate;
		}
		return template;
	}
}
