package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.spring.security.lti.openid.OIDCFilter;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinition;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
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
	final ToolDefinition toolDefinition;
	final ClaimAccessor claimAccessor;
	final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;

	public LTIApplicationSecurity(ToolDefinition toolDefinition, ClaimAccessor claimAccessor, DeepLinkingTokenBuilder deepLinkingTokenBuilder, ClientCredentialsTokenBuilder clientCredentialsTokenBuilder) {
		this.toolDefinition = toolDefinition;
		this.claimAccessor = claimAccessor;
		this.deepLinkingTokenBuilder = deepLinkingTokenBuilder;
		this.clientCredentialsTokenBuilder = clientCredentialsTokenBuilder;
	}

	protected LTIProcessingFilter getPreAuthFilter() throws Exception {
		LTIProcessingFilter preAuthFilter = new LTIProcessingFilter(toolDefinition, claimAccessor, deepLinkingTokenBuilder, clientCredentialsTokenBuilder);

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
		authenticationProvider.setPreAuthenticatedUserDetailsService(new LTIAuthenticationUserDetailsService<>(toolDefinition, claimAccessor, deepLinkingTokenBuilder, clientCredentialsTokenBuilder));

		auth.authenticationProvider(authenticationProvider);
	}

	private final static String OIDC_URI = "/login";


	private OIDCFilter oidcFilter() {
		return new OIDCFilter(OIDC_URI, toolDefinition, claimAccessor, deepLinkingTokenBuilder, clientCredentialsTokenBuilder);
	}
}