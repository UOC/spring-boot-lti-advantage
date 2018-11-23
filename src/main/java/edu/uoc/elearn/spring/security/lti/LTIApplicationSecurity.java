package edu.uoc.elearn.spring.security.lti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@ComponentScan("edu.uoc.elearn.spring.security.lti.mvc")
public class LTIApplicationSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	ToolDefinition toolDefinition;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LTIProcessingFilter preAuthFilter = new LTIProcessingFilter(toolDefinition);

		preAuthFilter.setCheckForPrincipalChanges(true);
		preAuthFilter.setAuthenticationManager(authenticationManager());

		http.addFilter(preAuthFilter);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
		authenticationProvider.setPreAuthenticatedUserDetailsService(new LTIAuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>(toolDefinition));

		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	@ConfigurationProperties(prefix = "lti")
	public ToolDefinition ltiTool() {
		return new ToolDefinition();
	}
}
