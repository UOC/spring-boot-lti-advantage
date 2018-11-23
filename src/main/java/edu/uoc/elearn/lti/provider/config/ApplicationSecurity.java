package edu.uoc.elearn.lti.provider.config;

import edu.uoc.elearn.spring.security.lti.LTIApplicationSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class ApplicationSecurity extends LTIApplicationSecurity {
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