package edu.uoc.elc.spring.lti.ags;

import edu.uoc.lti.ags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

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
