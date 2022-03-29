package edu.uoc.elc.spring.lti.security.openid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oidc.factory")
public class RequestAwareOIDCLaunchSessionBeanFactory implements FactoryBean<RequestAwareOIDCLaunchSession> {
	private final HttpSessionOIDCLaunchSession httpSessionOIDCLaunchSession;
	private final CachedOIDCLaunchSession cachedOIDCLaunchSession;

	@Getter
	@Setter
	@NotEmpty
	private String type;

	@Override
	public RequestAwareOIDCLaunchSession getObject() throws Exception {
		if ("cached".equals(type)) {
			return cachedOIDCLaunchSession;
		}
		return httpSessionOIDCLaunchSession;
	}

	@Override
	public Class<?> getObjectType() {
		return RequestAwareOIDCLaunchSession.class;
	}
}
