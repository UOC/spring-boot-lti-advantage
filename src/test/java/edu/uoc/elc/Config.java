package edu.uoc.elc;

import edu.uoc.elc.spring.lti.tool.BasicToolDefinition;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.accesstoken.JSONAccessTokenRequestBuilderImpl;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.jwt.claims.JWSClaimAccessor;
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder;
import edu.uoc.lti.jwt.deeplink.JWSTokenBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("edu.uoc.elc.spring.lti.tool")
@EnableConfigurationProperties
@PropertySource("application.properties")
@TestConfiguration
public class Config {
	@Bean
	public ClaimAccessor claimAccessor(BasicToolDefinition basicToolDefinition) {
		return new JWSClaimAccessor(basicToolDefinition.getKeySetUrl());
	}

	@Bean
	public DeepLinkingTokenBuilder deepLinkingTokenBuilder(BasicToolDefinition basicToolDefinition) {
		return new JWSTokenBuilder(basicToolDefinition.getPublicKey(), basicToolDefinition.getPrivateKey());
	}

	@Bean
	public ClientCredentialsTokenBuilder clientCredentialsTokenBuilder(BasicToolDefinition basicToolDefinition) {
		return new JWSClientCredentialsTokenBuilder(basicToolDefinition.getPublicKey(), basicToolDefinition.getPrivateKey());
	}

	@Bean
	public AccessTokenRequestBuilder accessTokenRequestBuilder() {
		return new JSONAccessTokenRequestBuilderImpl();
	}
}
