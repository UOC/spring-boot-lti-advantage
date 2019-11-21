package edu.uoc.elearn.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import lombok.Getter;

/**
 * Represents a Course from LTI context data
 *
 * @author xaracil@uoc.edu
 */
public class Context {
	private final Tool tool;
	@Getter
	private final edu.uoc.elc.lti.tool.Context ltiContext;


	public Context(Tool tool) {
		this.tool = tool;
		this.ltiContext = tool.getContext();
	}

	public String getName() {
		return ltiContext.getLabel();
	}

	public String getKey() {
		return ltiContext.getId();
	}

	public String getTitle() {
		return ltiContext.getTitle();
	}

	public Object getCustomParameter(String name) {
		return tool.getCustomParameter(name);
	}
}
