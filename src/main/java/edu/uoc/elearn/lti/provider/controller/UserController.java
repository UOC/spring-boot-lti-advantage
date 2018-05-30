package edu.uoc.elearn.lti.provider.controller;

import edu.uoc.elearn.openapi.spring.OpenApiAuthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Controller
@RequestMapping("/user")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UserController {
	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	OAuth2RestOperations openApiRestTemplate;

	@Value("${openapi.client.resource.user}")
	private String userResourceUrl;

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
	@OpenApiAuthorized
	public String init(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// request is for allowing redirect to this concrete url
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		response.getWriter().write(getUserData());
		return null;
	}

	private String getUserData() {
		Map userData = openApiRestTemplate.getForObject(userResourceUrl, Map.class);
		return userData.toString();
	}
}
