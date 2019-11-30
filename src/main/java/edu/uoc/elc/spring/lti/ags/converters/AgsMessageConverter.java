package edu.uoc.elc.spring.lti.ags.converters;

import edu.uoc.lti.ags.ContentTypes;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class AgsMessageConverter extends MappingJackson2HttpMessageConverter {
	private final ContentTypes contentType;

	public AgsMessageConverter(ContentTypes contentTypes) {
		super();
		this.contentType = contentTypes;
		setContentType();
	}

	private void setContentType() {
		setSupportedMediaTypes(Collections.singletonList(mediaType()));
	}

	private MediaType mediaType() {
		return new MediaType(contentType.getType(), contentType.getSubtype(), StandardCharsets.UTF_8);
	}
}
