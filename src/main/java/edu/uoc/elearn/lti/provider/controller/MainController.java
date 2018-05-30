package edu.uoc.elearn.lti.provider.controller;

import edu.uoc.elearn.lti.provider.security.AuthenticationHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/")
@Slf4j
@PreAuthorize("isAuthenticated")
public class MainController {

    @RequestMapping(method = RequestMethod.POST)
    public void init(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        AuthenticationHolder authenticationHolder = new AuthenticationHolder(request);

        response.getWriter().println(render_home(authenticationHolder)) ;
    }

    /**
     * Renders a home with requested data
     *
     * @param authenticationHolder
     * @return
     * @throws UnsupportedEncodingException
     */
    private String render_home(AuthenticationHolder authenticationHolder) throws UnsupportedEncodingException {

        String ret = "";
        if (authenticationHolder.isValid()) {
            ret += "Parameters\n" +
                    "    Fullname "+authenticationHolder.getFullName()+"\n" +
                    "    Is Admin "+authenticationHolder.isAdmin()+"\n" +
                    "    Is Instructor "+authenticationHolder.isInstructor()+"\n" +
                    "    Course Code "+authenticationHolder.getCourseKey()+"\n" +
                    "    Course Title "+authenticationHolder.getCourseTitle()+"\n" +
                    "";
        } else {
            ret = "Error not valid LTI";
        }
        return ret;

    }
}
