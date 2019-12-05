package edu.uoc.elc.spring.lti.security.openid;

import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class HttpSessionOIDCLaunchSession implements OIDCLaunchSession {
	private final static String STATE_SESSION_ATTRIBUTE_NAME = "currentLti1.3State";
	private final static String NONCE_SESSION_ATTRIBUTE_NAME = "currentLti1.3Nonce";
	private final static String TARGETLINK_URI_SESSION_ATTRIBUTE_NAME = "currentLti1.3TargetLinkUri";

	private final HttpServletRequest request;

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
		setAttribute(TARGETLINK_URI_SESSION_ATTRIBUTE_NAME, s);
	}

	private void setAttribute(String name, String value) {
		request.getSession().setAttribute(name, value);
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
		return getAttribute(TARGETLINK_URI_SESSION_ATTRIBUTE_NAME);
	}

	private String getAttribute(String name) {
		Object state = request.getSession().getAttribute(name);
		return state != null ? state.toString() : null;
	}
}
