package edu.uoc.elearn.lti.provider.config;

import edu.uoc.elearn.lti.provider.security.UOCLTIApplicationSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@Order(80)
public class ApplicationSecurity extends UOCLTIApplicationSecurity {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);

		http
						.formLogin()
						.loginPage("/login").permitAll()
						.failureUrl("/login?error").permitAll()

						.and()

						.logout()
						.permitAll()

						.and()

						.servletApi()
						.and()

						.authorizeRequests()
						.antMatchers("/css/**").permitAll()
						.antMatchers("/fonts/**").permitAll()
						.antMatchers("/img/**").permitAll()
						.antMatchers("/js/**").permitAll()
						.antMatchers("/login/**").permitAll()
						.antMatchers("/error/**").permitAll()
						.antMatchers("/error").permitAll()
						.antMatchers("/session-expired/**").permitAll()
						.anyRequest().fullyAuthenticated()

						.and()

						.csrf().disable();

	}
}