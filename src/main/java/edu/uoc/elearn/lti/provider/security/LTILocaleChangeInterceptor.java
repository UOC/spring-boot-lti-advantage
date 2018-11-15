package edu.uoc.elearn.lti.provider.security;

import edu.uoc.elc.lti.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Custom LocaleChangeInterceptor for setting the locale from LTI
 * Created by xavi on 23/9/16.
 */
@Slf4j
public class LTILocaleChangeInterceptor extends HandlerInterceptorAdapter {

	private LTITool ltiTool;

	public LTILocaleChangeInterceptor(LTITool ltiTool) {
		this.ltiTool = ltiTool;
	}

	private String getToken(HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getParameter("jwt");
		if (token == null || "".equals(token)) {
			token = httpServletRequest.getParameter("id_token");
		}
		return token;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Tool tool = new Tool(ltiTool.getName(), ltiTool.getClientId(), ltiTool.getKeySetUrl(), ltiTool.getAccessTokenUrl(), ltiTool.getPrivateKey(), ltiTool.getPublicKey());
		String token = getToken(request);
		tool.validate(token);
		if (tool.isValid()) {
			String newLocale = tool.getLocale();
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


