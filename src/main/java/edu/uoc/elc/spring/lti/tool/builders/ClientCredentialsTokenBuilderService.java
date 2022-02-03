package edu.uoc.elc.spring.lti.tool.builders;

import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public interface ClientCredentialsTokenBuilderService {
	ClientCredentialsTokenBuilder getClientCredentialsTokenBuilder(Registration registration);
}
