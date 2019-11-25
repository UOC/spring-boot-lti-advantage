package edu.uoc.elc.spring.security.lti.interceptors;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.spring.security.lti.tool.ToolDefinition;
import edu.uoc.elc.spring.security.lti.tool.ToolFactory;
import edu.uoc.elc.spring.security.lti.utils.RequestUtils;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
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

	private final ToolDefinition toolDefinition;
	private final ClaimAccessor claimAccessor;
	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;

	public LTILocaleChangeInterceptor(ToolDefinition toolDefinition, ClaimAccessor claimAccessor, DeepLinkingTokenBuilder deepLinkingTokenBuilder, ClientCredentialsTokenBuilder clientCredentialsTokenBuilder) {
		this.toolDefinition = toolDefinition;
		this.claimAccessor = claimAccessor;
		this.deepLinkingTokenBuilder = deepLinkingTokenBuilder;
		this.clientCredentialsTokenBuilder = clientCredentialsTokenBuilder;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		ToolFactory toolFactory = new ToolFactory();
		final Tool tool = toolFactory.from(toolDefinition, claimAccessor, deepLinkingTokenBuilder, clientCredentialsTokenBuilder, request);

		String token = RequestUtils.getToken(request);
		String state = request.getParameter("state");
		tool.validate(token, state);
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


