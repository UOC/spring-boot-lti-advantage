package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.lti.ags.AgsClient;
import edu.uoc.lti.ags.LineItem;
import edu.uoc.lti.ags.Result;
import edu.uoc.lti.ags.Score;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor(staticName = "of")
public class AgsClientAdaptor {
	private final OAuth2RestOperations restContainerTemplate;
	private final OAuth2RestOperations restItemTemplate;
	private final AssignmentGradeService assignmentGradeService;
	private final String resourceLinkId;
	private AgsClient agsClient;

	public List<LineItem> getLineItems(Integer limit, Integer page, String tag, String resourceId)  {
		return getAgsClient().getLineItems(limit, page, resourceLinkId, tag, resourceId);
	}

	public LineItem createLineItem(LineItem lineItem) {
		return getAgsClient().createLineItem(lineItem);
	}

	public LineItem getLineItem(String id) {
		return getAgsClient().getLineItem(id);
	}

	public LineItem updateLineItem(String id, LineItem lineItem) {
		return getAgsClient().updateLineItem(id, lineItem);
	}

	public void deleteLineItem(String id) {
		getAgsClient().deleteLineItem(id);
	}

	public List<Result> getLineItemResults(String id, Integer limit, Integer page, String userId) {
		return getAgsClient().getLineItemResults(id, limit, page, userId);
	}

	public boolean score(String lineItemId, Score score) {
		return getAgsClient().score(lineItemId, score);
	}

	AgsClient getAgsClient() {
		if (agsClient == null) {
			try {
				agsClient = RestTemplateAgsClient.of(restContainerTemplate,
								restItemTemplate,
								new URI(assignmentGradeService.getLineitems()),
								assignmentGradeService.canReadGrades(),
								assignmentGradeService.canReadLineItems(),
								assignmentGradeService.canManageLineItems(),
								assignmentGradeService.canScore());
			} catch (URISyntaxException e) {
				agsClient = new FailedAgsClient();
			}
		}
		return agsClient;
	}
}
