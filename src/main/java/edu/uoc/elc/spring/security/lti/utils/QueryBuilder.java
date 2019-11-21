package edu.uoc.elc.spring.security.lti.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class QueryBuilder {


	@SafeVarargs
	public static String of(SimpleImmutableEntry<String, Object>... parameters) throws UnsupportedEncodingException {
		if (parameters.length == 0) {
			return "";
		}

		StringBuilder query = new StringBuilder();
		for (SimpleImmutableEntry<String, Object> parameter : parameters) {
			if (parameter.getValue() != null) {
				query.append(parameter.getKey())
								.append("=")
								.append(URLEncoder.encode(parameter.getValue().toString(), "UTF-8"))
								.append("&");
			}
		}
		return query.toString();
	}
}
