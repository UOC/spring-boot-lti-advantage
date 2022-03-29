package edu.uoc.elc.spring.lti.security.openid;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@Service
public class CachedOIDCLaunchSession implements RequestAwareOIDCLaunchSession {
	public static final Map<String, OIDCLaunchData> cache = new HashMap<>();

	private OIDCLaunchData oidcLaunchData;
	private String state;

	@Override
	public void init(HttpServletRequest request) {
		this.oidcLaunchData = new OIDCLaunchData();
		this.state = request.getParameter("state");
	}

	@Override
	public void clear() {
		this.oidcLaunchData = new OIDCLaunchData();
		this.state = null;
	}

	@Override
	public void setRegistrationId(String s) {
		this.oidcLaunchData.registrationId = s;
	}

	@Override
	public String getRegistrationId() {
		return !StringUtils.isEmpty(this.state) ? getOIDCLaunchData().registrationId : this.oidcLaunchData.registrationId;
	}

	@Override
	public void setState(String s) {
		this.state = s;
		cache.put(state, oidcLaunchData);
	}

	@Override
	public void setNonce(String s) {
		getOIDCLaunchData().nonce = s;
	}

	@Override
	public void setTargetLinkUri(String s) {
		getOIDCLaunchData().targetLinkUri = s;
	}

	@Override
	public void setClientId(String s) {
		getOIDCLaunchData().clientId = s;
	}

	@Override
	public void setDeploymentId(String s) {
		getOIDCLaunchData().deploymentId = s;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public String getNonce() {
		return getOIDCLaunchData().nonce;
	}

	@Override
	public String getTargetLinkUri() {
		return getOIDCLaunchData().targetLinkUri;
	}

	@Override
	public String getClientId() {
		return getOIDCLaunchData().clientId;
	}

	@Override
	public String getDeploymentId() {
		return getOIDCLaunchData().deploymentId;
	}

	private OIDCLaunchData getOIDCLaunchData() {
		return this.cache.get(this.state);
	}

	private class OIDCLaunchData {
		String registrationId;
		String nonce;
		String targetLinkUri;
		String clientId;
		String deploymentId;
	}
}
