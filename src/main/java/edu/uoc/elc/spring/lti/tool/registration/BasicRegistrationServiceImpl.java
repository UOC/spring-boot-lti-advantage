package edu.uoc.elc.spring.lti.tool.registration;

import edu.uoc.elc.lti.tool.Key;
import edu.uoc.elc.lti.tool.Registration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@Getter
@Setter
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "lti.basic")
public class BasicRegistrationServiceImpl implements RegistrationService {
	private final RegistrationBean registration;

	@Override
	public Registration getRegistration(String id) {
		RegistrationFactory registrationFactory = new RegistrationFactory();
		return registrationFactory.from(getRegistration());
	}

	@Override
	public List<Key> getAllKeys() {
		RegistrationFactory registrationFactory = new RegistrationFactory();
		final Registration registration = registrationFactory.from(getRegistration());
		return registration.getKeySet().getKeys();
	}
}
