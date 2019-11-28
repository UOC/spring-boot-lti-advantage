package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.elc.lti.tool.ResourceLink;
import edu.uoc.elc.spring.security.lti.tool.AgsServiceProvider;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class TestAgsServiceProvider extends AgsServiceProvider {
	public TestAgsServiceProvider(AccessTokenProvider accessTokenProvider, AssignmentGradeService assignmentGradeService, ResourceLink resourceLink) {
		super(accessTokenProvider, assignmentGradeService, resourceLink);
	}

	@Override
	protected void initTemplateMessageConverter(OAuth2RestTemplate restTemplate, HttpMessageConverter messageConverter) {
		// nothing!!
	}
}
