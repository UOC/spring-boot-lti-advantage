package edu.uoc.elc.spring.lti.security.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class TokenFactory {
	public static String from(HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getParameter("jwt");
		if (token == null || "".equals(token)) {
			token = httpServletRequest.getParameter("id_token");
		}
		return token;
	}

}
