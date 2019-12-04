package edu.uoc.elc.spring.lti.ags;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.Collections;

/**
 * @author xaracil@uoc.edu
 */
public class RestTemplateFactory {
	public OAuth2RestTemplate from(AccessTokenProvider accessTokenProvider, HttpMessageConverter messageConverter) {
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		OAuth2ClientContext context = new DefaultOAuth2ClientContext();

		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);
		oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
		setMessageConverters(oAuth2RestTemplate, messageConverter);
		return oAuth2RestTemplate;
	}

	void setMessageConverters(OAuth2RestTemplate oAuth2RestTemplate, HttpMessageConverter messageConverter) {
		oAuth2RestTemplate.setMessageConverters(Collections.singletonList(messageConverter));
	}
}
