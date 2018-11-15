package edu.uoc.elearn.lti.provider.security;

import edu.uoc.elc.lti.tool.Tool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * UserDetails from LTI context data. Includes Course information
 *
 * @author xaracil@uoc.edu
 */
@Slf4j
public class LTIUserDetails extends User {

	@Getter
	private Tool tool;
	private LTICourse course;

	private String campusSessionId;

	private LTIUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	LTIUserDetails(String username, String password, Tool tool, String campusSessionId, Collection<? extends GrantedAuthority> authorities) {
		this(username, password, authorities);
		this.tool = tool;
		this.course = new LTICourse(tool);
		this.campusSessionId = campusSessionId;
	}

	LTICourse getCourse() {
		return course;
	}


	public String getCourseTitle() {
		return course.getTitle();
	}

	public String getEmail() {
		return tool.getUser().getEmail();
	}

	public String getFullName() {
		return tool.getUser().getName();
	}

	public URI getPhotoUrl() {
		try {
			return new URI(tool.getUser().getPicture());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	// UOC specific methods
    /*
    String getUserId() {
        return tool.getParameter("custom_id");
    }

    String getUserNumber() {
        return tool.getParameter("custom_usernumber");
    }

    String getUserName() {
        return tool.getParameter("custom_username");
    }

    public String getCustomParameter(String parameter) {
        return tool.getParameter("custom_" + parameter);
    }

    public Locale getLocale() {
        String ltiEnvironmentLocale = tool.getLocale();
        final String[] languangeAndCountry = ltiEnvironmentLocale.split("-");
        if (languangeAndCountry.length == 2) {
            return new Locale(languangeAndCountry[0], languangeAndCountry[1]);
        }

        return new Locale("es", "CA");
    }

    public String getCampusSessionId() {
        return this.campusSessionId;
        /*
        final String lis_person_sourcedid = tool.getParameter("lis_person_sourcedid");
        if (StringUtils.isEmpty(lis_person_sourcedid)) {
            return null;
        }
        return lis_person_sourcedid.split(":::").length > 1 ? lis_person_sourcedid.split(":::")[0] : null;
        */
/*    }

    }*/

    /*
    public List<org.oscelot.lti.tp.User> getMembers() {
        ToolProvider tool = tool.getTool();
        if (tool != null) {
            ResourceLink resourceLink = tool.getResourceLink();
            if (resourceLink.hasMembershipsService()) {
                return resourceLink.doMembershipsService(false);
            }
        }

        return new ArrayList<>();
    }*/
}
