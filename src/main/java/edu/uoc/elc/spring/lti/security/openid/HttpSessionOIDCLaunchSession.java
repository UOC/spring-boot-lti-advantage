package edu.uoc.elc.spring.lti.security.openid;

import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class HttpSessionOIDCLaunchSession implements OIDCLaunchSession {
	private final static String STATE_SESSION_ATTRIBUTE_NAME = "currentLti1.3State";
	private final static String NONCE_SESSION_ATTRIBUTE_NAME = "currentLti1.3Nonce";
	private final static String TARGET_LINK_URI_SESSION_ATTRIBUTE_NAME = "currentLti1.3TargetLinkUri";
	private final static String CLIENT_ID_SESSION_ATTRIBUTE_NAME = "currentLti1.3ClientId";
	private final static String DEPLOYMENT_ID_SESSION_ATTRIBUTE_NAME = "currentLti1.3DeploymentId";
	private final static String REGISTRATION_ID_SESSION_ATTRIBUTE_NAME = "currentLti1.3RegistrationId";

	private final HttpServletRequest request;

	public final static List<String> KEYS = Arrays.asList(STATE_SESSION_ATTRIBUTE_NAME, NONCE_SESSION_ATTRIBUTE_NAME, TARGET_LINK_URI_SESSION_ATTRIBUTE_NAME, CLIENT_ID_SESSION_ATTRIBUTE_NAME, DEPLOYMENT_ID_SESSION_ATTRIBUTE_NAME);

	public void clear() {
		final HttpSession session = this.request.getSession(false);
		if (session != null) {
			setState(null);
			setTargetLinkUri(null);
			setNonce(null);
		}
	}

	@Override
	public void setState(String s) {
		setAttribute(STATE_SESSION_ATTRIBUTE_NAME, s);
	}

	@Override
	public void setNonce(String s) {
		setAttribute(NONCE_SESSION_ATTRIBUTE_NAME, s);
	}

	@Override
	public void setTargetLinkUri(String s) {
		setAttribute(TARGET_LINK_URI_SESSION_ATTRIBUTE_NAME, s);
	}

	@Override
	public void setClientId(String s) {
		setAttribute(CLIENT_ID_SESSION_ATTRIBUTE_NAME, s);
	}

	@Override
	public void setDeploymentId(String s) {
		setAttribute(DEPLOYMENT_ID_SESSION_ATTRIBUTE_NAME, s);
	}

	private void setAttribute(String name, String value) {
		if (value == null) {
			request.getSession().removeAttribute(name);
		} else {
			request.getSession().setAttribute(name, value);
		}
	}

	@Override
	public String getState() {
		return getAttribute(STATE_SESSION_ATTRIBUTE_NAME);
	}

	@Override
	public String getNonce() {
		return getAttribute(NONCE_SESSION_ATTRIBUTE_NAME);
	}

	@Override
	public String getTargetLinkUri() {
		return getAttribute(TARGET_LINK_URI_SESSION_ATTRIBUTE_NAME);
	}

	@Override
	public String getClientId() {
		return getAttribute(CLIENT_ID_SESSION_ATTRIBUTE_NAME);
	}

	@Override
	public String getDeploymentId() {
		return getAttribute(DEPLOYMENT_ID_SESSION_ATTRIBUTE_NAME);
	}

	public void setRegistrationId(String s) {
		setAttribute(REGISTRATION_ID_SESSION_ATTRIBUTE_NAME, s);
	}

	public String getRegistrationId() {
		return getAttribute(REGISTRATION_ID_SESSION_ATTRIBUTE_NAME);
	}

	private String getAttribute(String name) {
		Object state = request.getSession().getAttribute(name);
		return state != null ? state.toString() : null;
	}
}
