package edu.uoc.elearn.lti.provider.controller;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elc.lti.platform.ags.LineItem;
import edu.uoc.elearn.lti.provider.security.UOCContext;
import edu.uoc.elearn.lti.provider.security.UOCUser;
import edu.uoc.elearn.spring.security.lti.tool.ToolProvider;
import edu.uoc.elearn.spring.security.lti.ags.AgsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class MainController {

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String init(UOCUser user, UOCContext context, ToolProvider toolProvider) throws IOException {
		return render_home(user, context, toolProvider);
	}

	/**
	 * Renders a home with requested data
	 *
	 * @return
	 */
	private String render_home(UOCUser user, UOCContext context, ToolProvider toolProvider) throws IOException {

		StringBuilder ret = new StringBuilder("<h1>Parameters:</h1>");
		ret.append("<ul>");
		ret.append("<li>Fullname: " + user.getUsername() + "</li>");
		ret.append("<li>Course Code " + context.getKey() + "</li>");
		ret.append("<li>Course Title " + context.getTitle() + "</li>");
		ret.append("</ul>");

		// add members
		final List<Member> members = toolProvider.getMembers();
		if (!CollectionUtils.isEmpty(members)) {
			ret.append("<h2>Members</h2>");
			ret.append("<ul>");
			for (Member member : members) {
				ret.append("<li>" + member.getName() + "<" + member.getEmail() + "></li>");
			}
			ret.append("</ul>");
		}

		// add line items
		ret.append("<h2>Line Items</h2>");
		final AgsClient assignmentAndGradeService = toolProvider.getAssignmentAndGradeService();
		if (!assignmentAndGradeService.canReadLineItems()) {
			ret.append("<p><strong>Can't read Line Items</p>");
		} else {
			final List<LineItem> lineItems = assignmentAndGradeService.getLineItems(null, null, null, null, null);
			ret.append("<ul>");
			for (LineItem lineItem : lineItems) {
				ret.append("<li>" + lineItem.getId() + ":" + lineItem.toString() +"></li>");
			}
			ret.append("</ul>");
		}

		return ret.toString();
	}
}
