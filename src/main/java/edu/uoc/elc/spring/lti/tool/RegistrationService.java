package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Key;
import edu.uoc.elc.lti.tool.Registration;

import java.util.List;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public interface RegistrationService {
	Registration getRegistration(String id);
	List<Key> getAllKeys();
}
