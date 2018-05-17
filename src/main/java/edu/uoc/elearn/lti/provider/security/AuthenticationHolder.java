package edu.uoc.elearn.lti.provider.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Holds an authentication and request
 * Allows searching for LTI user and ask for roles
 */
@Getter
@Slf4j
public class AuthenticationHolder {
    Authentication authentication;
    HttpServletRequest request;
    User user;

    public AuthenticationHolder(HttpServletRequest request) {
        this.request = request;
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        this.user = authentication.getPrincipal() instanceof User ? (User) authentication.getPrincipal() : null;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isInstructor() {
        return request.isUserInRole("ROLE_INSTRUCTOR");
    }

    public boolean isPRA() {
        return request.isUserInRole("ROLE_PRA");
    }

    public boolean isAdmin() {
        return request.isUserInRole("ROLE_ADMIN");
    }

    public boolean isTutor() {
        return request.isUserInRole("ROLE_TUTOR");
    }

    public boolean isTutorAdmin() {
        return request.isUserInRole("ROLE_TUTOR_ADMIN");
    }

    public boolean isUserValidAndInstructorOrPRA() {
        return user != null && (isInstructor() || isPRA());
    }

    public boolean isValid() {
        return user != null;
    }

    public boolean isLTI() {
        return user instanceof LTIUserDetails;
    }

    public String getHash() throws UnsupportedEncodingException {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        String message = ltiUserDetails.getUserId() + ltiUserDetails.getUserNumber();
        log.debug("Making hash of " + message);
        byte[] digest = DigestUtils.md5Digest(message.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte aDigest : digest) {
            sb.append(Integer.toHexString((aDigest & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public String getUsername() {
        return isValid() ? user.getUsername() : null;
    }

    public String getCampusUserName() {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        return ltiUserDetails.getUserName();
    }

    public String getFullName() {
        if (!isLTI()) {
            return request.getUserPrincipal().getName();
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        return ltiUserDetails.getFullName();
    }

    public String getCampusSessionId() {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        return ltiUserDetails.getCampusSessionId();
    }

    @SuppressWarnings("WeakerAccess")
    public LTICourse getCourse() {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        return ltiUserDetails.getCourse();
    }

    public String getCourseKey() {
        return getCourse() != null ? getCourse().getKey() : null;
    }

    public String getCourseTitle() {
        return getCourse() != null ? getCourse().getTitle() : null;
    }

    public String getSemester() {
        return getCourse() != null ? getCourse().getSemester() : null;
    }

    public boolean isOnlyTutor() {
        return !isAdmin() && !isTutorAdmin() && isTutor();
    }

    public String getLanguage() {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        return ltiUserDetails.getLocale().getLanguage();
    }

    /**
     * Gets the course instructor login if course is TUTORIA
     *
     * @return instructor's login
     */
    public String getCourseInstructor() {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        if (ltiUserDetails.getCourse().isTutoria()) {
            String code = ltiUserDetails.getCourse().getCampusCode();
            final int i = code.indexOf("_");
            return code.substring(i + 1);
        }
        return null;
    }

    public String getCustomParameter(String parameter) {
        if (!isLTI()) {
            return null;
        }
        LTIUserDetails ltiUserDetails = (LTIUserDetails) user;
        return ltiUserDetails.getCustomParameter(parameter);
    }
}
