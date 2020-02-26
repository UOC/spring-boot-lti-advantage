package edu.uoc.elc.spring.lti.security.utils;

import javax.servlet.http.HttpServletRequest;

import lombok.experimental.UtilityClass;

/**
 * @author xaracil@uoc.edu
 */
@UtilityClass
public class TokenFactory {
	public String from(HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getParameter("jwt");
		if (token == null || "".equals(token)) {
			token = httpServletRequest.getParameter("id_token");
		}
		return token;
	}

}
