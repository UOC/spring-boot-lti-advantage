package edu.uoc.elearn.lti.provider.security;

import edu.uoc.lti.LTIEnvironment;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 * LTI Pre Auth filter. Tries to perform a preauth using LTI validation
 *
 * @author xaracil@uoc.edu
 */
public class LTIBasedPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    public LTIBasedPreAuthenticatedProcessingFilter(List<String> adminUsers, List<String> adminDomainCodes) {
        super();
        setAuthenticationDetailsSource(new LTIBasedPreAuthenticatedWebAuthenticationDetailsSource(adminUsers, adminDomainCodes));
    }

    public LTIBasedPreAuthenticatedProcessingFilter() {
        this(null, null);
    }

    private String getConfigurationFile() throws URISyntaxException {
        File file = new File(this.getClass().getResource("/authorizedConsumersKey.properties").toURI());

        return file.getAbsolutePath();
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        LTIEnvironment ltiEnvironment;
        try {
            ltiEnvironment = new LTIEnvironment(getConfigurationFile());
            ltiEnvironment.parseRequest(httpServletRequest);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Checking if request is a valid LTI");
            }
            if (/*ltiEnvironment.is_lti_request(httpServletRequest) && */ltiEnvironment.isAuthenticated()) {
                this.logger.info("Valid LTI call from " + ltiEnvironment.getUserName());
                return ltiEnvironment.getUserName() + "-" + ltiEnvironment.getCourseKey();
            }
            this.logger.info("The request is not a valid LTI one");
        } catch (URISyntaxException use) {
            this.logger.error("Error paring configuration file "+use.getMessage(), use);
        }
        return null;
    }

    // Store LTI context in credentials
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        try {
            LTIEnvironment ltiEnvironment = new LTIEnvironment(getConfigurationFile());
            ltiEnvironment.parseRequest(httpServletRequest);
            if (ltiEnvironment.is_lti_request(httpServletRequest) && ltiEnvironment.isAuthenticated()) {
                return httpServletRequest;
            }
        } catch (URISyntaxException use) {
            this.logger.error("Error paring configuration file "+use.getMessage(), use);
        }

        return "{ N.A. }";
    }
}
