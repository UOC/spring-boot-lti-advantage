package edu.uoc.elc.spring.lti.security.openid;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@Service
public class HttpSessionStateRelatedOIDCLaunchSession implements RequestAwareOIDCLaunchSession {

	public final static String PREFIX = "currentLTI1.3";
	private final static String STATE_SUFFIX = "State";
	private final static String NONCE_SUFFIX = "Nonce";
	private final static String TARGET_LINK_URI_SUFFIX = "TargetLinkUri";
	private final static String CLIENT_ID_SUFFIX = "ClientId";
	private final static String DEPLOYMENT_ID_SUFFIX = "DeploymentId";
	private final static String REGISTRATION_ID_SUFFIX = "RegistrationId";

	private HttpServletRequest request;
	private String state;

	@Override
	public void clear() {
		final HttpSession session = this.request.getSession(false);
		if (session != null) {
			setState(null);
			setTargetLinkUri(null);
			setNonce(null);
			setClientId(null);
			setRegistrationId(null);
			setDeploymentId(null);
		}
	}

	@Override
	public void init(HttpServletRequest request) {
		this.request = request;
		this.state = request.getParameter("state");
	}

	@Override
	public void setState(String s) {
		this.state = s;
		setAttribute(STATE_SUFFIX, s);
	}

	@Override
	public void setNonce(String s) {
		setAttribute(NONCE_SUFFIX, s);
	}

	@Override
	public void setTargetLinkUri(String s) {
		setAttribute(TARGET_LINK_URI_SUFFIX, s);
	}

	@Override
	public void setClientId(String s) {
		setAttribute(CLIENT_ID_SUFFIX, s);
	}

	@Override
	public void setDeploymentId(String s) {
		setAttribute(DEPLOYMENT_ID_SUFFIX, s);
	}

	private void setAttribute(String suffix, String value) {
		final String name = getAttributeName(suffix);
		if (value == null) {
			request.getSession().removeAttribute(name);
		} else {
			request.getSession().setAttribute(name, value);
		}
	}

	private String getAttributeName(String suffix) {
		return PREFIX + this.state + suffix;
	}

	@Override
	public String getState() {
		return getAttribute(STATE_SUFFIX);
	}

	@Override
	public String getNonce() {
		return getAttribute(NONCE_SUFFIX);
	}

	@Override
	public String getTargetLinkUri() {
		return getAttribute(TARGET_LINK_URI_SUFFIX);
	}

	@Override
	public String getClientId() {
		return getAttribute(CLIENT_ID_SUFFIX);
	}

	@Override
	public String getDeploymentId() {
		return getAttribute(DEPLOYMENT_ID_SUFFIX);
	}

	@Override
	public void setRegistrationId(String s) {
		setAttribute(REGISTRATION_ID_SUFFIX, s);
	}

	@Override
	public String getRegistrationId() {
		return getAttribute(REGISTRATION_ID_SUFFIX);
	}

	private String getAttribute(String name) {
		Object state = request.getSession().getAttribute(name);
		return state != null ? state.toString() : null;
	}
}
