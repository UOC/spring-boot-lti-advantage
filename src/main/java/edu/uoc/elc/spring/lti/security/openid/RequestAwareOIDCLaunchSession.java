package edu.uoc.elc.spring.lti.security.openid;

import edu.uoc.lti.oidc.OIDCLaunchSession;

import javax.servlet.http.HttpServletRequest;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public interface RequestAwareOIDCLaunchSession extends OIDCLaunchSession {
	void init(HttpServletRequest request);
	void clear();

	void setRegistrationId(String s);

	String getRegistrationId();
}
