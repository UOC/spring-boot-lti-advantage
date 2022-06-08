package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.elc.lti.tool.ToolBuilders;
import edu.uoc.elc.spring.lti.security.openid.RequestAwareOIDCLaunchSession;
import edu.uoc.elc.spring.lti.security.openid.RequestAwareOIDCLaunchSessionBeanFactory;
import edu.uoc.elc.spring.lti.tool.builders.ClaimAccessorService;
import edu.uoc.elc.spring.lti.tool.builders.ClientCredentialsTokenBuilderService;
import edu.uoc.elc.spring.lti.tool.builders.DeepLinkingTokenBuilderService;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xaracil@uoc.edu
 */
@Component
@RequiredArgsConstructor
public class ToolDefinitionBean {
	@Getter
	private final ClaimAccessorService claimAccessor;

	private final DeepLinkingTokenBuilderService deepLinkingTokenBuilder;
	private final ClientCredentialsTokenBuilderService clientCredentialsTokenBuilder;
	private final AccessTokenRequestBuilder accessTokenRequestBuilder;

	@Resource(name = "&requestAwareOIDCLaunchSession")
	private RequestAwareOIDCLaunchSessionBeanFactory factory;

	public ToolBuilders getBuilders(Registration registration, String kid) {
		return new ToolBuilders(clientCredentialsTokenBuilder.getClientCredentialsTokenBuilder(registration, kid),
						accessTokenRequestBuilder,
						deepLinkingTokenBuilder.getDeepLinkingTokenBuilder(registration, kid));
	}

	public RequestAwareOIDCLaunchSession getRequestAwareOIDCLaunchSession() {
		// gotten requestAwareOIDCLaunchSession through factory for allowing prototype scope,
		// so we can create a new RequestAwareOIDCLaunchSession for each request
		return factory.getObject();
	}
}
