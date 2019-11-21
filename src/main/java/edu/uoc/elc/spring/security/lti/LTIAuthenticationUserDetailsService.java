package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinition;
import edu.uoc.elc.spring.security.lti.tool.ToolFactory;
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
	private final ToolDefinition toolDefinition;

	public LTIAuthenticationUserDetailsService(ToolDefinition toolDefinition) {
		this.toolDefinition = toolDefinition;
	}

	@Override
	public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
		if (authentication.getCredentials() instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) authentication.getCredentials();
			ToolFactory toolFactory = new ToolFactory();
			final Tool tool = toolFactory.from(toolDefinition, request);

			if (tool.isValid()) {
				Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				if (authentication.getDetails() instanceof PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) {
					PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details = (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) authentication.getDetails();
					authorities = details.getGrantedAuthorities();
				}

				// create user details
				return new User(authentication.getName(), "N. A.", tool, authorities);
			}
		}
		return null;
	}
}
