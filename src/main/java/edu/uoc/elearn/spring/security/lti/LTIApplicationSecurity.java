package edu.uoc.elearn.spring.security.lti;

import edu.uoc.elearn.spring.security.lti.openid.OIDCFilter;
import edu.uoc.elearn.spring.security.lti.tool.ToolDefinition;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Configuration
@ComponentScan(value = {"edu.uoc.elearn.spring.security.lti.mvc", "edu.uoc.elearn.spring.security.lti.tool"})
public class LTIApplicationSecurity extends WebSecurityConfigurerAdapter {
	@Getter
	@Autowired
	ToolDefinition toolDefinition;

	protected LTIProcessingFilter getPreAuthFilter() throws Exception {
		LTIProcessingFilter preAuthFilter = new LTIProcessingFilter(toolDefinition);

		preAuthFilter.setCheckForPrincipalChanges(true);
		preAuthFilter.setAuthenticationManager(authenticationManager());
		return preAuthFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final LTIProcessingFilter preAuthFilter = getPreAuthFilter();

		http.addFilter(preAuthFilter).addFilterAfter(oidcFilter(), preAuthFilter.getClass());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
		authenticationProvider.setPreAuthenticatedUserDetailsService(new LTIAuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>(toolDefinition));

		auth.authenticationProvider(authenticationProvider);
	}

	private final static String OIDC_URI = "/login";


	private OIDCFilter oidcFilter() {
		final OIDCFilter oidcFilter = new OIDCFilter(OIDC_URI, toolDefinition);
		return oidcFilter;
	}
}
