package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.elc.spring.security.lti.utils.QueryBuilder;
import edu.uoc.lti.ags.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor(staticName = "of")
public class RestTemplateScoreServiceClient implements ScoreServiceClient {
	private final OAuth2RestOperations restTemplate;

	/**
	 * Publishes a score update. Tool platform may decide to change the result value based on the updated score.
	 * @param lineItemId Id of the line item
	 * @param score the score to update
	 * @return true if the score was updated. false otherwise
	 */
	@Override
	public boolean score(String lineItemId, Score score) {
		String uri = String.format( "%s/scores", lineItemId);
		restTemplate.postForLocation(uri, score);
		return true;
	}
}
