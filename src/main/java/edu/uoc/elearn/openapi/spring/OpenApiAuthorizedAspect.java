package edu.uoc.elearn.openapi.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Aspect
@Component
public class OpenApiAuthorizedAspect implements InitializingBean {
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	private RequestCache requestCache = new HttpSessionRequestCache();

	private String redirectUri;

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}


	@Around("@annotation(OpenApiAuthorized)")
	public Object redirectIfNotLogged(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		if (oauth2ClientContext.getAccessToken() == null) {
			final HttpServletRequest request = getRequest(proceedingJoinPoint.getArgs());
			if (request != null) {
				this.requestCache.saveRequest(request, null);
			}

			final Class<?> returnType = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getReturnType();
			if (returnType.getName().equals("void")) {
				final HttpServletResponse response = getResponse(proceedingJoinPoint.getArgs());
				if (response != null) {
					response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", redirectUri);
					return null;
				}
			}
			if (returnType.isAssignableFrom(ModelAndView.class)) {
				return new ModelAndView("redirect:" + redirectUri);
			}
			if (returnType.isAssignableFrom(String.class)) {
				return "redirect:" + redirectUri;
			}
			return null;
		}
		return proceedingJoinPoint.proceed();
	}

	private HttpServletRequest getRequest(Object[] args) {
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				return (HttpServletRequest) arg;
			}
		}
		return null;
	}

	private HttpServletResponse getResponse(Object[] args) {
		for (Object arg : args) {
			if (arg instanceof HttpServletResponse) {
				return (HttpServletResponse) arg;
			}
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.state(this.redirectUri != null, "Supply a redirectUri");
	}
}
