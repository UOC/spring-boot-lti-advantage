package edu.uoc.elc.spring.lti.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.lti.namesrole.ContentTypes;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author xaracil@uoc.edu
 */
public class NamesRoleServiceMessageConverter extends MappingJackson2HttpMessageConverter {
	public NamesRoleServiceMessageConverter() {
		super();
		setContentType();
	}

	public NamesRoleServiceMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
		setContentType();
	}

	private void setContentType() {
		setSupportedMediaTypes(Collections.singletonList(mediaType(ContentTypes.REQUEST)));
	}

	private MediaType mediaType(ContentTypes contentTypes) {
		return new MediaType(contentTypes.getType(), contentTypes.getSubtype(), StandardCharsets.UTF_8);
	}
}
