package edu.uoc.elc.spring.lti.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@Configuration
@RequiredArgsConstructor
public class RegistrationServiceBeanConfig {
	private final BasicRegistrationServiceImpl basicRegistrationService;
	private final RegistrationServiceImpl registrationService;

	@Bean(name = "registrationService")
	public RegistrationServiceBeanFactory registrationServiceBeanFactory() {
		return new RegistrationServiceBeanFactory(basicRegistrationService, registrationService);
	}

	@Bean
	public RegistrationService registrationService() throws Exception {
		return registrationServiceBeanFactory().getObject();
	}
}
