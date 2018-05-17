package edu.uoc.elearn.lti.provider.security;

import edu.uoc.lti.LTIEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.oscelot.lti.tp.ResourceLink;
import org.oscelot.lti.tp.ToolProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * UserDetails from LTI context data. Includes Course information
 *
 * @author xaracil@uoc.edu
 */
@Slf4j
public class LTIUserDetails extends User {

    private LTIEnvironment ltiEnvironment;
    private LTICourse course;

    private String campusSessionId;

    private LTIUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    LTIUserDetails(String username, String password, LTIEnvironment ltiEnvironment, String campusSessionId, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities);
        this.ltiEnvironment = ltiEnvironment;
        this.course = new LTICourse(ltiEnvironment);
        this.campusSessionId = campusSessionId;
    }

    LTICourse getCourse() {
        return course;
    }


    public String getCourseTitle() {
        return course.getTitle();
    }

    // UOC specific methods
    String getUserId() {
        return ltiEnvironment.getParameter("custom_id");
    }

    String getUserNumber() {
        return ltiEnvironment.getParameter("custom_usernumber");
    }

    String getUserName() {
        return ltiEnvironment.getParameter("custom_username");
    }

    public String getEmail() {
        return ltiEnvironment.getEmail();
    }

    public String getCustomParameter(String parameter) {
        return ltiEnvironment.getParameter("custom_" + parameter);
    }

    public String getFullName() {
        return ltiEnvironment.getFullName();
    }

    public Locale getLocale() {
        String ltiEnvironmentLocale = ltiEnvironment.getLocale();
        final String[] languangeAndCountry = ltiEnvironmentLocale.split("-");
        if (languangeAndCountry.length == 2) {
            return new Locale(languangeAndCountry[0], languangeAndCountry[1]);
        }

        return new Locale("es", "CA");
    }

    public String getCampusSessionId() {
        return this.campusSessionId;
        /*
        final String lis_person_sourcedid = ltiEnvironment.getParameter("lis_person_sourcedid");
        if (StringUtils.isEmpty(lis_person_sourcedid)) {
            return null;
        }
        return lis_person_sourcedid.split(":::").length > 1 ? lis_person_sourcedid.split(":::")[0] : null;
        */
    }

    public URI getPhotoUrl() {
        try {
            return new URI(ltiEnvironment.getUser_image());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public List<org.oscelot.lti.tp.User> getMembers() {
        ToolProvider toolProvider = ltiEnvironment.getToolProvider();
        if (toolProvider != null) {
            ResourceLink resourceLink = toolProvider.getResourceLink();
            if (resourceLink.hasMembershipsService()) {
                return resourceLink.doMembershipsService(false);
            }
        }

        return new ArrayList<>();
    }
}
