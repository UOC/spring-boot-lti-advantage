package edu.uoc.elc.spring.security.lti.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class RequestUtils {
	public static String getToken(HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getParameter("jwt");
		if (token == null || "".equals(token)) {
			token = httpServletRequest.getParameter("id_token");
		}
		return token;
	}

}
