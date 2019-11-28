package edu.uoc.elc.spring.security.lti.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.lti.ags.ContentTypes;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class AgsItemServiceMessageConverter extends MappingJackson2HttpMessageConverter {
	public AgsItemServiceMessageConverter() {
		super();
		setContentType();
	}

	public AgsItemServiceMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
		setContentType();
	}

	private void setContentType() {
		setSupportedMediaTypes(Collections.singletonList(mediaType(ContentTypes.ITEM)));
	}

	private MediaType mediaType(ContentTypes contentTypes) {
		return new MediaType(contentTypes.getType(), contentTypes.getSubtype(), StandardCharsets.UTF_8);
	}
}
