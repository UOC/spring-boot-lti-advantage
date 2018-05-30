package edu.uoc.elearn.openapi.spring;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class OpenApiClientFilter extends GenericFilterBean implements ApplicationEventPublisherAware {
	public OAuth2RestOperations restTemplate;
	private RequestMatcher requiresAuthenticationRequestMatcher;
	private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	private ApplicationEventPublisher eventPublisher;

	public OpenApiClientFilter(String defaultFilterProcessesUrl) {
		this.setFilterProcessesUrl(defaultFilterProcessesUrl);
	}

	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void afterPropertiesSet() throws ServletException {
		Assert.state(this.restTemplate != null, "Supply a rest-template");
		super.afterPropertiesSet();
	}

	public void setFilterProcessesUrl(String filterProcessesUrl) {
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
	}

	public final void setRequiresAuthenticationRequestMatcher(RequestMatcher requestMatcher) {
		Assert.notNull(requestMatcher, "requestMatcher cannot be null");
		this.requiresAuthenticationRequestMatcher = requestMatcher;
	}

	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return this.requiresAuthenticationRequestMatcher.matches(request);
	}

	private void publish(ApplicationEvent event) {
		if (this.eventPublisher != null) {
			this.eventPublisher.publishEvent(event);
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		if (!this.requiresAuthentication(request, response)) {
			filterChain.doFilter(request, response);
		} else {
			OAuth2AccessToken accessToken;
			try {
				accessToken = this.restTemplate.getAccessToken();
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
				successHandler.onAuthenticationSuccess(request, response, null);
			} catch (OAuth2Exception var7) {
				BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", var7);
				this.publish(new OAuth2AuthenticationFailureEvent(bad));
				throw bad;
			}
		}
	}
}
