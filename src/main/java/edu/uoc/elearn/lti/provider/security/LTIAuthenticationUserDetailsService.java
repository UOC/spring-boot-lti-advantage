package edu.uoc.elearn.lti.provider.security;

import edu.uoc.elc.lti.tool.Tool;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * UserDetailsService from LTI
 *
 * @author xaracil@uoc.edu
 */
public class LTIAuthenticationUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T> {
	private LTITool ltiTool;

	public LTIAuthenticationUserDetailsService(LTITool ltiTool) {
		this.ltiTool = ltiTool;
	}

	private String getToken(HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getParameter("jwt");
		if (token == null || "".equals(token)) {
			token = httpServletRequest.getParameter("id_token");
		}
		return token;
	}

	@Override
	public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
		if (authentication.getCredentials() instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) authentication.getCredentials();
			Tool tool = new Tool(ltiTool.getName(), ltiTool.getClientId(), ltiTool.getKeySetUrl(), ltiTool.getAccessTokenUrl(), ltiTool.getPrivateKey(), ltiTool.getPublicKey());

			String token = getToken(request);
			tool.validate(token);

			if (tool.isValid()) {
				Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				if (authentication.getDetails() instanceof PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) {
					PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details = (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) authentication.getDetails();
					authorities = details.getGrantedAuthorities();
				}

				// create user details
				String campusSessionId = tool.getCustomParameter("sessionid") != null ? tool.getCustomParameter("sessionid").toString() : null;
				return new LTIUserDetails(authentication.getName(), "N. A.", tool, campusSessionId, authorities);
			}
		}
		return null;
	}
}
