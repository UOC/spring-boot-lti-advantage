package edu.uoc.elc.spring.security.lti.tool;

import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.elc.lti.tool.ResourceLink;
import edu.uoc.elc.spring.security.lti.ags.AgsClientAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
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
	private final ResourceLink resourceLink;

	private OAuth2RestOperations containerTemplate;
	private OAuth2RestOperations intemTemplate;

	public AgsClientAdaptor getAssignmentAndGradeServiceClient() {
		return AgsClientAdaptor.of(getContainerTemplate(), getItemTemplate(), getAssignmentGradeService(), getResourceLinkId());
	}

	private OAuth2RestOperations getContainerTemplate() {
		if (containerTemplate == null) {
			containerTemplate = createTemplate(new AgsContainerServiceMessageConverter());
		}
		return containerTemplate;
	}

	private OAuth2RestOperations getItemTemplate() {
		if (intemTemplate == null) {
			intemTemplate = createTemplate(new AgsItemServiceMessageConverter());
		}
		return intemTemplate;
	}

	private OAuth2RestTemplate createTemplate(HttpMessageConverter messageConverter) {
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		OAuth2ClientContext context = new DefaultOAuth2ClientContext();

		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
		oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
		initTemplateMessageConverter(oAuth2RestTemplate, messageConverter);
		return oAuth2RestTemplate;
	}

	protected void initTemplateMessageConverter(OAuth2RestTemplate restTemplate, HttpMessageConverter messageConverter) {
		restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
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

	private String getResourceLinkId() {
		return resourceLink != null ? resourceLink.getId() : null;
	}
}
