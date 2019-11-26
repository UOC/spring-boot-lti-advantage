package edu.uoc.elc.spring.security.lti;

import edu.uoc.elc.lti.tool.Tool;
import lombok.Getter;

import java.util.List;

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

	public String getId() {
		return this.ltiContext != null ? this.ltiContext.getId() : null;
	}

	public String getLabel() {
		return this.ltiContext != null ? this.ltiContext.getLabel() : null;
	}

	public String getTitle() {
		return this.ltiContext != null ? this.ltiContext.getTitle() : null;
	}

	public List<String> getType() {
		return this.ltiContext != null ? this.ltiContext.getType() : null;
	}

	public Object getCustomParameter(String name) {
		return tool.getCustomParameter(name);
	}
}
