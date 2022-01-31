package edu.uoc.elc.spring.lti.tool;

import edu.uoc.lti.namesrole.ContentTypes;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public class NamesRoleServiceContentTypeInterceptor extends ContentTypeInterceptor {
	public NamesRoleServiceContentTypeInterceptor() {
		super(new MediaType(ContentTypes.REQUEST.getType(), ContentTypes.REQUEST.getSubtype(), StandardCharsets.UTF_8));
	}
}
