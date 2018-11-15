package edu.uoc.elearn.lti.provider.controller;

import edu.uoc.elc.lti.platform.Member;
import edu.uoc.elearn.lti.provider.security.*;
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
	public String init(LTIUserDetails user, LTICourse course, SpringLTIToolProvider toolProvider) throws IOException {
		return render_home(user, course, toolProvider);
	}



	/**
	 * Renders a home with requested data
	 *
	 * @return
	 */
	private String render_home(LTIUserDetails user, LTICourse course, SpringLTIToolProvider toolProvider) throws IOException {

		StringBuilder ret = new StringBuilder("<h1>Parameters:</h1>");
		ret.append("<ul>");
		ret.append("<li>Fullname: " + user.getUsername() + "</li>");
		ret.append("<li>Course Code " + course.getKey() + "</li>");
		ret.append("<li>Course Title " + course.getTitle() + "</li>");
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
		return ret.toString();
	}
}
