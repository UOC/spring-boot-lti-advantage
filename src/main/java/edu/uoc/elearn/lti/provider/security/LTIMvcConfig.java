package edu.uoc.elearn.lti.provider.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Configuration
public class LTIMvcConfig implements WebMvcConfigurer {
	@Autowired
	private CurrentCourseHandlerMethodArgumentResolver currentCourseHandlerMethodArgumentResolver;

	@Autowired
	private CurrentLTIUserHandlerMethodArgumentResolver currentLTIUserHandlerMethodArgumentResolver;

	@Autowired
	private CurrentToolHandlerMethodArgumentResolver currentToolHandlerMethodArgumentResolver;

	public LTIMvcConfig() {
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(this.currentCourseHandlerMethodArgumentResolver);
		argumentResolvers.add(this.currentLTIUserHandlerMethodArgumentResolver);
		argumentResolvers.add(this.currentToolHandlerMethodArgumentResolver);
	}

}
