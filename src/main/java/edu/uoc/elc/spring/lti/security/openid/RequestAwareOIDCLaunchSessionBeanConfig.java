package edu.uoc.elc.spring.lti.security.openid;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@Configuration
@RequiredArgsConstructor
public class RequestAwareOIDCLaunchSessionBeanConfig {
	private final CachedOIDCLaunchSession cachedOIDCLaunchSession;
	private final HttpSessionOIDCLaunchSession httpSessionOIDCLaunchSession;

	@Bean(name = "requestAwareOIDCLaunchSession")
	public RequestAwareOIDCLaunchSessionBeanFactory requestAwareOIDCLaunchSessionBeanFactory() {
		return new RequestAwareOIDCLaunchSessionBeanFactory(httpSessionOIDCLaunchSession, cachedOIDCLaunchSession);
	}

	@Bean
	public RequestAwareOIDCLaunchSession requestAwareOIDCLaunchSession() throws Exception {
		return requestAwareOIDCLaunchSessionBeanFactory().getObject();
	}
}
