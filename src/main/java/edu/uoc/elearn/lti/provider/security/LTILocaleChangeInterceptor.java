package edu.uoc.elearn.lti.provider.security;

import edu.uoc.lti.LTIEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Custom LocaleChangeInterceptor for setting the locale from LTI
 * Created by xavi on 23/9/16.
 */
@Slf4j
public class LTILocaleChangeInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            ServletException {
        LTIEnvironment ltiEnvironment = new LTIEnvironment(request);
        if (ltiEnvironment.isAuthenticated()) {
            String newLocale = ltiEnvironment.getLocale();
            log.info(newLocale);
            if (newLocale != null) {
                LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
                if (localeResolver == null) {
                    throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
                }

                final String[] languangeAndCountry = newLocale.split("-");
                if (languangeAndCountry.length == 2) {
                    log.info("Setting locale to " + newLocale);
                    localeResolver.setLocale(request, response, new Locale(languangeAndCountry[0], languangeAndCountry[1]));
                }
            }
        }

        return true;
    }
}


