package edu.uoc.elc.spring.lti.tool;

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
@ConfigurationProperties(prefix = "registration")
public class RegistrationServiceBeanFactory implements FactoryBean<RegistrationService> {
	private final BasicRegistrationServiceImpl basicRegistrationService;
	private final RegistrationServiceImpl registrationService;

	@Getter
	@Setter
	@NotEmpty
	private String type;

	@Override
	public RegistrationService getObject() {
		if (type.equals("multiple")) {
			return registrationService;
		}
		return basicRegistrationService;
	}

	@Override
	public Class<?> getObjectType() {
		return RegistrationService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
