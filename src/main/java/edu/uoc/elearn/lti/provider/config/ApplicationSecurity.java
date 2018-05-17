package edu.uoc.elearn.lti.provider.config;

import edu.uoc.elearn.lti.provider.security.LTIAuthenticationUserDetailsService;
import edu.uoc.elearn.lti.provider.security.LTIBasedPreAuthenticatedProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
//@EnableWebMvcSecurity Deprecated
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LTIBasedPreAuthenticatedProcessingFilter preAuthFilter = new LTIBasedPreAuthenticatedProcessingFilter(null, null);
        preAuthFilter.setCheckForPrincipalChanges(true);
        preAuthFilter.setAuthenticationManager(authenticationManager());

        http
                .addFilter(preAuthFilter)
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new LTIAuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>());

        auth.authenticationProvider(authenticationProvider);
    }
}