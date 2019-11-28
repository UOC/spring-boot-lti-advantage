package edu.uoc.elc.spring.security.lti.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
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
		setSupportedMediaTypes(Arrays.asList(
						new MediaType("application", "vnd.ims.lti-nrps.v2.membershipcontainer+json", StandardCharsets.UTF_8),
						new MediaType("application", "vnd.ims-nrps.v2.membershipcontainer+json", StandardCharsets.UTF_8)
		));

	}
}
