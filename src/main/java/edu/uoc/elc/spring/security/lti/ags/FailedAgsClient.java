package edu.uoc.elc.spring.security.lti.ags;

import edu.uoc.lti.ags.AgsClient;
import edu.uoc.lti.ags.LineItem;
import edu.uoc.lti.ags.Result;
import edu.uoc.lti.ags.Score;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class FailedAgsClient implements AgsClient {
	@Override
	public List<LineItem> getLineItems(Integer integer, Integer integer1, String s, String s1, String s2) {
		throw new MethodNotAllowedException("GET", null);
	}

	@Override
	public LineItem createLineItem(LineItem lineItem) {
		throw new MethodNotAllowedException("POST", null);
	}

	@Override
	public LineItem getLineItem(String s) {
		throw new MethodNotAllowedException("GET", null);
	}

	@Override
	public LineItem updateLineItem(String s, LineItem lineItem) {
		throw new MethodNotAllowedException("PUT", null);
	}

	@Override
	public void deleteLineItem(String s) {
		throw new MethodNotAllowedException("DELETE", null);
	}

	@Override
	public List<Result> getLineItemResults(String s, Integer integer, Integer integer1, String s1) {
		throw new MethodNotAllowedException("GET", null);
	}

	@Override
	public boolean score(String s, Score score) {
		throw new MethodNotAllowedException("POST", null);
	}
}
