package edu.uoc.elc.spring.security.lti.openid;

import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class HttpSessionOIDCLaunchSession implements OIDCLaunchSession {
	private final static String STATE_SESSION_ATTRIBUTE_NAME = "currentLti1.3State";
	private final static String NONCE_SESSION_ATTRIBUTE_NAME = "currentLti1.3Nonce";

	private final HttpServletRequest request;

	@Override
	public void setState(String s) {
		request.getSession().setAttribute(STATE_SESSION_ATTRIBUTE_NAME, s);
	}

	@Override
	public void setNonce(String s) {
		request.getSession().setAttribute(NONCE_SESSION_ATTRIBUTE_NAME, s);
	}

	@Override
	public String getState() {
		Object state = request.getSession().getAttribute(STATE_SESSION_ATTRIBUTE_NAME);
		return state != null ? state.toString() : null;
	}

	@Override
	public String getNonce() {
		Object state = request.getSession().getAttribute(NONCE_SESSION_ATTRIBUTE_NAME);
		return state != null ? state.toString() : null;
	}
}
