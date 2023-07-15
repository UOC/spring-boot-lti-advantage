package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elc.lti.platform.NamesRoleServiceResponse;
import edu.uoc.elc.lti.tool.NamesRoleService;
import edu.uoc.elc.lti.tool.ResourceLink;
import edu.uoc.elc.lti.tool.RolesEnum;
import edu.uoc.lti.namesrole.ContentTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class NamesRoleServiceProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final NamesRoleService namesRoleService;
	private final ResourceLink resourceLink;
	private final String ROLE_PARAMETER = "role";

	private OAuth2RestOperations template;

	/**
	 * Call to platform's Name and Role Service in order to get members
	 * @return the members of the platform
	 */
	public List<Member> getMembers() throws URISyntaxException {
		return getMembers(true);
	}

	/**
	 * Call to platform's Name and Role Service in order to get members allowing set parameters to filter
	 * @param params Map wuth key and value parameters
	 * @return
	 * @return the members of the platform
	 */
	public List<Member> getMembers(@NotNull Map<String, String> params) throws URISyntaxException {
		return getMembers(params, true);
	}

	/**
	 * Call to platform's Name and Role Service in order to get learners
	 * @return the members of the platform
	 */
	public List<Member> getLearners() throws URISyntaxException {
		return getLearners(true);
	}

	/**
	 * Call to platform's Name and Role Service in order to get instructors
	 * @return the members of the platform
	 */
	public List<Member> getInstructors() throws URISyntaxException {
		return getInstructors(true);
	}

	/**
	 * Call to platform's Name and Role Service in order to get members
	 * @return the members of the platform
	 */
	public List<Member> getMembers(boolean addResourceLink) throws URISyntaxException {
		if (!hasNameRoleService()) {
			return Collections.EMPTY_LIST;
		}

		final URI membershipUrl = getPlatformUri(null, addResourceLink);
		return getMembersFromServer(membershipUrl);
	}

	/**
	 * Call to platform's Name and Role Service in order to get members allowing set parameters to filter
	 * @param params Map wuth key and value parameters
	 * @return
	 * @return the members of the platform
	 */
	public List<Member> getMembers(@NotNull Map<String, String> params, boolean addResourceLink) throws URISyntaxException {
		if (!hasNameRoleService()) {
			return Collections.EMPTY_LIST;
		}

		URI membershipUrl = getPlatformUri(params, addResourceLink);
		return getMembersFromServer(membershipUrl);
	}

	/**
	 * Call to platform's Name and Role Service in order to get learners
	 * @return the members of the platform
	 */
	public List<Member> getLearners(boolean addResourceLink) throws URISyntaxException {
		if (!hasNameRoleService()) {
			return Collections.EMPTY_LIST;
		}

		final Map<String, String> params = new HashMap<String, String>() {{
			put(ROLE_PARAMETER, RolesEnum.LEARNER.getName());
		}};
		final URI membershipUrl = getPlatformUri(params, addResourceLink);

		return getMembersFromServer(membershipUrl);
	}

	/**
	 * Call to platform's Name and Role Service in order to get instructors
	 * @return the members of the platform
	 */
	public List<Member> getInstructors(boolean addResourceLink) throws URISyntaxException {
		if (!hasNameRoleService()) {
			return Collections.EMPTY_LIST;
		}

		final Map<String, String> params = new HashMap<String, String>() {{
			put(ROLE_PARAMETER, RolesEnum.INSTRUCTOR.getName());
		}};
		final URI membershipUrl = getPlatformUri(params, addResourceLink);

		return getMembersFromServer(membershipUrl);
	}

	public boolean hasNameRoleService() {
		return namesRoleService != null;
	}

	private URI getPlatformUri(Map<String, String> params, boolean addResourceLink) throws URISyntaxException {
		URI membershipUrl = new URI(namesRoleService.getContext_memberships_url());

		if (resourceLink != null && addResourceLink) {
			membershipUrl = appendResourceLinkIdToQuery(membershipUrl);
		}

		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				membershipUrl = UriComponentsBuilder.fromUri(membershipUrl).queryParam(entry.getKey(), entry.getValue()).build().toUri();
			}
		}
		return membershipUrl;
	}

	private URI appendResourceLinkIdToQuery(URI uri) {
		return UriComponentsBuilder.fromUri(uri).queryParam("rlid", resourceLink.getId()).build().toUri();
	}

	private List<Member> getMembersFromServer(URI uri) {
		final OAuth2RestOperations restOperations = getTemplate();

		final NamesRoleServiceResponse namesRoleServiceResponse = restOperations.getForObject(uri, NamesRoleServiceResponse.class);
		return Objects.requireNonNull(namesRoleServiceResponse).getMembers();
	}

	private OAuth2RestOperations getTemplate() {
		if (template == null) {
			final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
			OAuth2ClientContext context = new DefaultOAuth2ClientContext();

			final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
			oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
			oAuth2RestTemplate.setInterceptors(Collections.singletonList(new NamesRoleServiceContentTypeInterceptor()));
			oAuth2RestTemplate.setMessageConverters(Collections.singletonList(new NamesRoleServiceMessageConverter()));
			template = oAuth2RestTemplate;
		}
		return template;
	}
}
