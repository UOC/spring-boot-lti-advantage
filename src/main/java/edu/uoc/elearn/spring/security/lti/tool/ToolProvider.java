package edu.uoc.elearn.spring.security.lti.tool;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elc.lti.platform.NamesRoleServiceResponse;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elearn.spring.security.lti.LTIAccessTokenProvider;
import edu.uoc.elearn.spring.security.lti.ags.AgsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class ToolProvider {
	private final Tool tool;

	private OAuth2RestOperations template;

	/**
	 * Call to platform's Name and Role Service in order to get members
	 * @return the members of the platform
	 */
	public List<Member> getMembers() {
		// call to NamesRoleService
		final OAuth2RestOperations restOperations = getTemplate();

		final String membershipUrl = tool.getNameRoleService().getContext_memberships_url();

		final NamesRoleServiceResponse namesRoleServiceResponse = restOperations.getForObject(membershipUrl, NamesRoleServiceResponse.class);
		return namesRoleServiceResponse.getMembers();
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
