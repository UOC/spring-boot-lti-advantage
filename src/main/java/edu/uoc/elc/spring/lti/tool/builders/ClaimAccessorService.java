package edu.uoc.elc.spring.lti.tool.builders;

import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.lti.claims.ClaimAccessor;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public interface ClaimAccessorService {
	ClaimAccessor getClaimAccessor(Registration registration);
}
