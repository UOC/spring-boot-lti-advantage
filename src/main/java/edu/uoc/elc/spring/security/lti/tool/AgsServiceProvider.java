package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.elc.spring.security.lti.ags.RestTemplateAgsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.Collections;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class AgsServiceProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final AssignmentGradeService assignmentGradeService;

	private OAuth2RestOperations template;

	private OAuth2RestOperations getTemplate() {
		if (template == null) {
			final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
			OAuth2ClientContext context = new DefaultOAuth2ClientContext();

			final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
			oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
			template = oAuth2RestTemplate;
		}
		return template;
	}

	public RestTemplateAgsClient getAssignmentAndGradeServiceClient() {
		return RestTemplateAgsClient.of(getTemplate(), getAssignmentGradeService());
	}

	private AssignmentGradeService getAssignmentGradeService() {
		if (assignmentGradeService != null) {
			return assignmentGradeService;
		}
		return defaultAssignmentGradeService();
	}

	private AssignmentGradeService defaultAssignmentGradeService() {
		final AssignmentGradeService assignmentGradeService = new AssignmentGradeService();
		assignmentGradeService.setScope(Collections.EMPTY_LIST);
		return assignmentGradeService;
	}
}
