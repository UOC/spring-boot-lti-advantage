package edu.uoc.elearn.lti.provider.security;

import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elearn.spring.security.lti.Context;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a Course from LTI context data
 *
 * @author xaracil@uoc.edu
 */
@Slf4j
public class UOCContext extends Context {

	public UOCContext(Tool tool) {
		super(tool);
	}

	public String getCode() {
		return getCustomParameter("domain_coditercers") != null ? getCustomParameter("domain_coditercers").toString() : null;
	}

	public String getSemester() {
		return getCustomParameter("domain_domainlogofile") != null ? getCustomParameter("domain_domainlogofile").toString() : null;
	}

	public String getCampusCode() {
		return getCustomParameter("domain_code") != null ? getCustomParameter("domain_code").toString() : null;
	}

	public String getDomainType() {
		return getCustomParameter("domain_type") != null ? getCustomParameter("domain_type").toString() : null;
	}

	public boolean isTutoria() {
		return "TUTORIA".equals(getDomainType());
	}
}
