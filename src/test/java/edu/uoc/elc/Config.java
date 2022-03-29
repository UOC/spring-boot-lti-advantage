package edu.uoc.elc;

import edu.uoc.elc.lti.tool.Key;
import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.elc.spring.lti.tool.builders.ClaimAccessorService;
import edu.uoc.elc.spring.lti.tool.builders.ClientCredentialsTokenBuilderService;
import edu.uoc.elc.spring.lti.tool.builders.DeepLinkingTokenBuilderService;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.accesstoken.JSONAccessTokenRequestBuilderImpl;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.jwt.claims.JWSClaimAccessor;
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder;
import edu.uoc.lti.jwt.deeplink.JWSTokenBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author xaracil@uoc.edu
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({"edu.uoc.elc.spring.lti.tool", "edu.uoc.elc.spring.lti.security.openid"})
@EnableConfigurationProperties
@PropertySource("application.properties")
@TestConfiguration
public class Config {

	@Value("${lineItemsUri}")
	private String lineItemsUri;

	@Bean
	public ClaimAccessorService claimAccessorService() {
		return registration -> new JWSClaimAccessor(registration.getKeySetUrl());
	}

	@Bean
	public DeepLinkingTokenBuilderService deepLinkingTokenBuilderService() {
		return (registration, kid) -> {
			final Key key = registration.getKeySet().getKeys().get(0);
			return new JWSTokenBuilder(key.getPublicKey(), key.getPrivateKey(), key.getAlgorithm());
		};
	}

	@Bean
	public ClientCredentialsTokenBuilderService clientCredentialsTokenBuilderService() {
		return (registration, kid) -> {
			final Key key = registration.getKeySet().getKeys().get(0);
			return new JWSClientCredentialsTokenBuilder(key.getPublicKey(), key.getPrivateKey(), key.getAlgorithm());
		};
	}

	@Bean
	public AccessTokenRequestBuilder accessTokenRequestBuilder() {
		return new JSONAccessTokenRequestBuilderImpl();
	}

	@Bean
	public String lineItemsUri() {
		return this.lineItemsUri;
	}
}
