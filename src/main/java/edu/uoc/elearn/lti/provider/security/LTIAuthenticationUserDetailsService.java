package edu.uoc.elearn.lti.provider.security;

import edu.uoc.lti.LTIEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

/**
 * UserDetailsService from LTI
 *
 * @author xaracil@uoc.edu
 */
public class LTIAuthenticationUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T> {

    private String getConfigurationFile() throws URISyntaxException {
        File file = new File(this.getClass().getResource("/authorizedConsumersKey.properties").toURI());

        return file.getAbsolutePath();
    }

    @Override
    public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
        if (authentication.getCredentials() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) authentication.getCredentials();
            LTIEnvironment ltiEnvironment;
            try {
                ltiEnvironment = new LTIEnvironment(getConfigurationFile());

                ltiEnvironment.parseRequest(request);
                if (ltiEnvironment.isAuthenticated()) {
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    if (authentication.getDetails() instanceof PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) {
                        PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details = (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) authentication.getDetails();
                        authorities = details.getGrantedAuthorities();
                    }

                    // create user details
                    return new LTIUserDetails(authentication.getName(), "N. A.", ltiEnvironment, ltiEnvironment.getCustomParameter("sessionid", request), authorities);
                }
            } catch (URISyntaxException use) {
                throw new UsernameNotFoundException("Error paring configuration file "+use.getMessage());
            }
        }
        return null;
    }
}
