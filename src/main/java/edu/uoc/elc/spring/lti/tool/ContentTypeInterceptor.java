package edu.uoc.elc.spring.lti.tool;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
@RequiredArgsConstructor
public class ContentTypeInterceptor implements ClientHttpRequestInterceptor {
	private final MediaType contentType;

	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		final HttpHeaders headers = httpRequest.getHeaders();
		headers.setContentType(contentType);
		return clientHttpRequestExecution.execute(httpRequest, bytes);
	}
}
