package edu.uoc.elearn.lti.provider.security;

import edu.uoc.elc.lti.tool.Context;
import edu.uoc.elc.lti.tool.Tool;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a Course from LTI context data
 *
 * @author xaracil@uoc.edu
 */
@Slf4j
public class LTICourse {
    private final Tool tool;
    private final Context ltiContext;

    public LTICourse(Tool tool) {
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

    public String getCode() {
        return tool.getCustomParameter("domain_coditercers") != null ? tool.getCustomParameter("domain_coditercers").toString() : null;
    }

    public String getSemester() {
        return tool.getCustomParameter("domain_domainlogofile") != null ? tool.getCustomParameter("domain_domainlogofile").toString() : null;
    }

    public String getCampusCode() {
        return tool.getCustomParameter("domain_code") != null ? tool.getCustomParameter("domain_code").toString() : null;
    }

    public String getDomainType() {
        return tool.getCustomParameter("domain_type") != null ? tool.getCustomParameter("domain_type").toString() : null;
    }

    public boolean isTutoria() {
        return "TUTORIA".equals(getDomainType());
    }
}
