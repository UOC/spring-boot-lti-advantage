package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.spring.security.lti.openid.OIDCFilter;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinitionBean;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Configuration
@ComponentScan(value = {"edu.uoc.elc.spring.security.lti.mvc", "edu.uoc.elc.spring.security.lti.tool"})
public class LTIApplicationSecurity extends WebSecurityConfigurerAdapter {
	@Getter
	final ToolDefinitionBean toolDefinitionBean;

	public LTIApplicationSecurity(ToolDefinitionBean toolDefinitionBean) {
		this.toolDefinitionBean = toolDefinitionBean;
	}

	protected LTIProcessingFilter getPreAuthFilter() throws Exception {
		LTIProcessingFilter preAuthFilter = new LTIProcessingFilter(toolDefinitionBean);

		preAuthFilter.setCheckForPrincipalChanges(true);
		preAuthFilter.setAuthenticationManager(authenticationManager());
		return preAuthFilter;
	}

	private final static String OIDC_LAUNCH_URL = "/oidclaunch";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final LTIProcessingFilter preAuthFilter = getPreAuthFilter();

		http.addFilter(preAuthFilter)
						.addFilterAfter(oidcFilter(), preAuthFilter.getClass())
						.authorizeRequests()
						.antMatchers(OIDC_LAUNCH_URL).permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
		authenticationProvider.setPreAuthenticatedUserDetailsService(new LTIAuthenticationUserDetailsService<>());

		auth.authenticationProvider(authenticationProvider);
	}

	private OIDCFilter oidcFilter() {
		return new OIDCFilter(OIDC_LAUNCH_URL, toolDefinitionBean);
	}
}
