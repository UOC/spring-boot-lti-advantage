package edu.uoc.elearn.lti.provider.security;

import edu.uoc.lti.LTIEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.Attributes2GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * AuthenticationDetailsSource from LTI
 *
 * @author xaracil@uoc.edu
 */
class LTIBasedPreAuthenticatedWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final Attributes2GrantedAuthoritiesMapper ltiUserRoles2GrantedAuthoritiesMapper = new SimpleAttributes2GrantedAuthoritiesMapper();

    private final List<String> adminUsers;

    private final List<String> adminDomainCodes;

    public LTIBasedPreAuthenticatedWebAuthenticationDetailsSource() {
        this(null, null);
    }

    public LTIBasedPreAuthenticatedWebAuthenticationDetailsSource(List<String> adminUsers, List<String> adminDomainCodes) {
        this.adminUsers = adminUsers;
        this.adminDomainCodes = adminDomainCodes;
    }

    private String getConfigurationFile() {
        URL resource = getClass().getResource("/authorizedConsumersKey.properties");
        return resource.getPath();
    }

    private Collection<String> getUserRoles(HttpServletRequest request) {
        ArrayList<String> ltiUserRolesList = new ArrayList<>();

        LTIEnvironment ltiEnvironment = new LTIEnvironment(getConfigurationFile());
        ltiEnvironment.parseRequest(request);

        if (ltiEnvironment.isAuthenticated()) {
            ltiUserRolesList.add("USER");
        }
        if (ltiEnvironment.isCourseAuthorized() && !ltiEnvironment.isInstructor()) {
            ltiUserRolesList.add("STUDENT");
        }
        if (ltiEnvironment.isInstructor()) {
            String specificRolesParam = ltiEnvironment.getCustomParameter("specific_role", request);
            List<String> specificRoles = specificRolesParam != null ? Arrays.asList(specificRolesParam.split(",")) : null;
            if ("TUTORIA".equals(ltiEnvironment.getCustomParameter("domain_typeid", request))) {
                ltiUserRolesList.add("TUTOR");

                // check if we're assigned as ADMINISTRACIO
                if (specificRoles != null && specificRoles.contains("ADMINISTRACIO")) {
                    ltiUserRolesList.add("TUTOR_ADMIN");
                }
            } else {

                // PRA
                if (specificRoles != null && specificRoles.contains("CREADOR")) {
                    ltiUserRolesList.add("PRA");
                } else {
                    ltiUserRolesList.add("INSTRUCTOR");
                }
            }
        }

        if (isAdmin(ltiEnvironment.getUserName(), ltiEnvironment.getCustomParameter("username", request), ltiEnvironment.getCustomParameter("domain_code", request))) {
            ltiUserRolesList.add("ADMIN");
        }

        return ltiUserRolesList;
    }

    private boolean isAdmin(String userName, String customUserName, String domainCode) {
        // super admin case
        if ("admin".equals(userName) || "admin".equals(customUserName)) {
            return true;
        }

        if (this.adminDomainCodes != null) {
            if (this.adminDomainCodes.contains(domainCode)) {
                return true;
            }
            ;
        }
        if (this.adminUsers != null) {
            return this.adminUsers.contains(userName) || this.adminUsers.contains(customUserName);
        }

        return false;
    }

    @Override
    public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(HttpServletRequest httpServletRequest) {
        Collection<String> ltiUserRoles = this.getUserRoles(httpServletRequest);
        final Collection<? extends GrantedAuthority> userGas = this.ltiUserRoles2GrantedAuthoritiesMapper.getGrantedAuthorities(ltiUserRoles);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("LTI roles [" + ltiUserRoles + "] mapped to Granted Authorities: [" + userGas + "]");
        }

        return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(httpServletRequest, userGas);
    }
}
