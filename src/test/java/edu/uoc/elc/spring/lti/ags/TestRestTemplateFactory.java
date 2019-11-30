package edu.uoc.elc.spring.lti.ags;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class TestRestTemplateFactory extends RestTemplateFactory {
	@Override
	void setMessageConverters(OAuth2RestTemplate oAuth2RestTemplate, HttpMessageConverter messageConverter) {
		// nothing here
	}
}
