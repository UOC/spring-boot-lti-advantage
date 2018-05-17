package edu.uoc.elearn.lti.provider.security;

import edu.uoc.lti.LTIEnvironment;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a Course from LTI context data
 *
 * @author xaracil@uoc.edu
 */
@Slf4j
class LTICourse {
    private final LTIEnvironment ltiEnvironment;

    LTICourse(LTIEnvironment ltiEnvironment) {
        this.ltiEnvironment = ltiEnvironment;
    }

    public String getName() {
        return ltiEnvironment.getCourseName();
    }

    public String getKey() {
        String key = ltiEnvironment.getCourseKey();
        final String[] strings = key.split("_");
        return strings[strings.length - 1];
    }

    public String getTitle() {
        return ltiEnvironment.getCourseTitle();
    }

    public String getCode() {
        return ltiEnvironment.getParameter("custom_domain_coditercers");
    }

    public String getSemester() {
        return ltiEnvironment.getParameter("custom_domain_domainlogofile");
    }

    public String getCampusCode() {
        return ltiEnvironment.getParameter("custom_domain_code");
    }

    public String getDomainType() {
        return ltiEnvironment.getParameter("custom_domain_type");
    }

    public boolean isTutoria() {
        return "TUTORIA".equals(getDomainType());
    }
}
