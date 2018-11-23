package edu.uoc.elearn.spring.security.lti.interceptors;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elearn.spring.security.lti.ToolDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class LTILocaleChangeInterceptor extends HandlerInterceptorAdapter {
	protected final Log logger = LogFactory.getLog(this.getClass());

	private ToolDefinition toolDefinition;

	public LTILocaleChangeInterceptor(ToolDefinition toolDefinition) {
		this.toolDefinition = toolDefinition;
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
		Tool tool = new Tool(toolDefinition.getName(), toolDefinition.getClientId(), toolDefinition.getKeySetUrl(), toolDefinition.getAccessTokenUrl(), toolDefinition.getPrivateKey(), toolDefinition.getPublicKey());
		String token = getToken(request);
		tool.validate(token);
		if (tool.isValid()) {
			String newLocale = tool.getLocale();
			if (newLocale != null) {
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
				if (localeResolver == null) {
					throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
				}

				final String[] languangeAndCountry = newLocale.split("-");
				if (languangeAndCountry.length == 2) {
					if (this.logger.isDebugEnabled()) {
						this.logger.debug("Setting locale to " + newLocale);
					}
					localeResolver.setLocale(request, response, new Locale(languangeAndCountry[0], languangeAndCountry[1]));
				}
			}
		}

		return true;
	}
}


