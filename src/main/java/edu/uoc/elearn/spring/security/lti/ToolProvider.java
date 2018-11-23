package edu.uoc.elearn.spring.security.lti;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elc.lti.platform.NamesRoleServiceResponse;
import edu.uoc.elc.lti.tool.Tool;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.io.IOException;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@AllArgsConstructor
public class ToolProvider {
	private Tool tool;

	public List<Member> getMembers() throws IOException {
		// call to NamesRoleService
		final OAuth2RestOperations restOperations = getTemplate();

		final String membershipUrl = tool.getNameRoleService().getContext_memberships_url();

		final NamesRoleServiceResponse namesRoleServiceResponse = restOperations.getForObject(membershipUrl, NamesRoleServiceResponse.class);
		return namesRoleServiceResponse.getMembers();
	}

	private OAuth2RestOperations getTemplate() {
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		OAuth2ClientContext context = new DefaultOAuth2ClientContext();

		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
		oAuth2RestTemplate.setAccessTokenProvider(new LTIAccessTokenProvider(tool));
		return  oAuth2RestTemplate;
	}
}
