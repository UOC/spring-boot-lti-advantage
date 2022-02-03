package edu.uoc.elc.spring.lti.tool.registration;

import edu.uoc.elc.lti.tool.Key;
import edu.uoc.elc.lti.tool.Registration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@Getter
@Setter
@Service
@ConfigurationProperties(prefix = "lti")
public class RegistrationServiceImpl implements RegistrationService {

	private List<RegistrationBean> registrations;

	public Registration getRegistration(String id) {
		RegistrationFactory registrationFactory = new RegistrationFactory();
		return Objects.nonNull(registrations) ? registrations.stream().filter(registration -> id.equals(registration.getId())).map(registrationFactory::from).findFirst().orElse(null) : null;
	}

	public List<Key> getAllKeys() {
		RegistrationFactory registrationFactory = new RegistrationFactory();
		return Objects.nonNull(registrations) ? registrations.stream().map(registrationFactory::from).flatMap(registration -> registration.getKeySet().getKeys().stream()).collect(Collectors.toList()) : Collections.emptyList();
	}
}
