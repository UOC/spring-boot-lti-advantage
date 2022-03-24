package edu.uoc.elc.spring.lti.security;

import edu.uoc.elc.spring.lti.security.openid.OIDCFilter;
import edu.uoc.elc.spring.lti.tool.registration.RegistrationService;
import edu.uoc.elc.spring.lti.tool.ToolDefinitionBean;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * @author xaracil@uoc.edu
 */
@Configuration
@ComponentScan(value = {"edu.uoc.elc.spring.lti.security.mvc", "edu.uoc.elc.spring.lti.tool"})
public class LTIApplicationSecurity extends WebSecurityConfigurerAdapter {
	@Getter
	final RegistrationService registrationService;
	final ToolDefinitionBean toolDefinitionBean;

	public LTIApplicationSecurity(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") RegistrationService registrationService,
																ToolDefinitionBean toolDefinitionBean) {
		this.registrationService = registrationService;
		this.toolDefinitionBean = toolDefinitionBean;
	}

	protected LTIProcessingFilter getPreAuthFilter() throws Exception {
		LTIProcessingFilter preAuthFilter = new LTIProcessingFilter(registrationService, toolDefinitionBean);

		preAuthFilter.setCheckForPrincipalChanges(true);
		preAuthFilter.setAuthenticationManager(authenticationManager());
		return preAuthFilter;
	}

	private final static String OIDC_LAUNCH_URL = "/oidclaunch";
	private final static String OIDC_LAUNCH_URL_WITH_REGISTRATION = "/oidclaunch/{registrationId:.*}";
	private final static String OIDC_LAUNCH_PREFIX = "/oidclaunch";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final LTIProcessingFilter preAuthFilter = getPreAuthFilter();

		http.addFilter(preAuthFilter)
						.addFilterAfter(oidcFilter(), preAuthFilter.getClass())
						.authorizeRequests()
						.antMatchers(OIDC_LAUNCH_URL).permitAll()
						.antMatchers(OIDC_LAUNCH_URL_WITH_REGISTRATION).permitAll();
	}

	@Autowired
	public void configureGlobal(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") AuthenticationManagerBuilder auth) {
		PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
		authenticationProvider.setPreAuthenticatedUserDetailsService(new LTIAuthenticationUserDetailsService<>());

		auth.authenticationProvider(authenticationProvider);
	}

	private OIDCFilter oidcFilter() {
		final OIDCFilter oidcFilter = new OIDCFilter(OIDC_LAUNCH_URL, OIDC_LAUNCH_PREFIX, registrationService, toolDefinitionBean);
		oidcFilter.setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(
						new AntPathRequestMatcher(OIDC_LAUNCH_URL)
						, new AntPathRequestMatcher(OIDC_LAUNCH_URL_WITH_REGISTRATION)
		));
		return oidcFilter;
	}
}
