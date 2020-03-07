package edu.uoc.elc.spring.lti.tool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.util.UriComponentsBuilder;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elc.lti.platform.NamesRoleServiceResponse;
import edu.uoc.elc.lti.tool.NamesRoleService;
import edu.uoc.elc.lti.tool.ResourceLink;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class NamesRoleServiceProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final NamesRoleService namesRoleService;
	private final ResourceLink resourceLink;

	private OAuth2RestOperations template;

	/**
	 * Call to platform's Name and Role Service in order to get members
	 * 
	 * @return the members of the platform
	 */
	public List<Member> getMembers() throws URISyntaxException {
		if (!hasNameRoleService()) {
			return Collections.emptyList();
		}

		final URI membershipUrl = getPlatformUri();
		return getMembersFromServer(membershipUrl);
	}

	public boolean hasNameRoleService() {
		return namesRoleService != null;
	}

	private URI getPlatformUri() throws URISyntaxException {
		URI membershipUrl = new URI(namesRoleService.getContext_memberships_url());

		if (resourceLink != null) {
			membershipUrl = appendResourceLinkIdToQuery(membershipUrl);
		}
		return membershipUrl;
	}

	private URI appendResourceLinkIdToQuery(URI uri) {
		return UriComponentsBuilder.fromUri(uri).queryParam("rlid", resourceLink.getId()).build().toUri();
	}

	private List<Member> getMembersFromServer(URI uri) {
		final OAuth2RestOperations restOperations = getTemplate();
		final NamesRoleServiceResponse namesRoleServiceResponse = restOperations.getForObject(uri,
				NamesRoleServiceResponse.class);
		return Objects.requireNonNull(namesRoleServiceResponse).getMembers();
	}

	private OAuth2RestOperations getTemplate() {
		if (template == null) {
			final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
			OAuth2ClientContext context = new DefaultOAuth2ClientContext();

			final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
			oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
			oAuth2RestTemplate.setMessageConverters(Collections.singletonList(new NamesRoleServiceMessageConverter()));
			template = oAuth2RestTemplate;
		}
		return template;
	}
}
