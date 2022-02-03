package edu.uoc.elc.spring.lti.tool.builders;

import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public interface DeepLinkingTokenBuilderService {
	DeepLinkingTokenBuilder getDeepLinkingTokenBuilder(Registration registration);
}
