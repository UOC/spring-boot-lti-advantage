package edu.uoc.elearn.lti.provider.security.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Configuration
public class UOCLTIMvcConfig implements WebMvcConfigurer {
	@Autowired
	private CurrentUOCContextHandlerMethodArgumentResolver currentUOCContextHandlerMethodArgumentResolver;

	@Autowired
	private CurrentUOCUserHandlerMethodArgumentResolver currentUOCUserHandlerMethodArgumentResolver;


	public UOCLTIMvcConfig() {
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(this.currentUOCContextHandlerMethodArgumentResolver);
		argumentResolvers.add(this.currentUOCUserHandlerMethodArgumentResolver);
	}

}
