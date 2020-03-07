package edu.uoc.elc.spring.lti.security.interceptors;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import edu.uoc.elc.spring.lti.security.User;
import lombok.extern.apachecommons.CommonsLog;

/**
 * Custom LocaleChangeInterceptor for setting the locale from LTI Created by
 * xavi on 23/9/16.
 */
@CommonsLog
public class LTILocaleChangeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			User user = (User) ((Authentication) principal).getPrincipal();
			if (user != null) {
				String newLocale = user.getLocale();
				if (newLocale != null) {
					LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
					if (localeResolver == null) {
						throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
					}

					final String[] languangeAndCountry = newLocale.split("-");
					if (languangeAndCountry.length == 2) {
						if (log.isDebugEnabled()) {
							log.debug("Setting locale to " + newLocale);
						}
						localeResolver.setLocale(request, response,
								new Locale(languangeAndCountry[0], languangeAndCountry[1]));
					}
				}
			}
		}

		return true;
	}
}
