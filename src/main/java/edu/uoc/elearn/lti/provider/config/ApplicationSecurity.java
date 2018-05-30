package edu.uoc.elearn.lti.provider.config;

import edu.uoc.elearn.lti.provider.security.LTIAuthenticationUserDetailsService;
import edu.uoc.elearn.lti.provider.security.LTIBasedPreAuthenticatedProcessingFilter;
import edu.uoc.elearn.openapi.spring.OpenApiAuthorizedAspect;
import edu.uoc.elearn.openapi.spring.OpenApiClientFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Client
@EnableAspectJAutoProxy
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    OAuth2ClientContext oauth2ClientContext;

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

                .and().addFilterBefore(openapiFilter(), BasicAuthenticationFilter.class)

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

    private final static String OPEN_API_URI = "/login/openapi";

    private OpenApiClientFilter openapiFilter() {
        OpenApiClientFilter openApiFilter = new OpenApiClientFilter(OPEN_API_URI);
        OAuth2RestTemplate openapiRestTemplate = new OAuth2RestTemplate(openapi(), oauth2ClientContext);
        openApiFilter.setRestTemplate(openapiRestTemplate);
        return openApiFilter;
    }

    @Bean
    @ConfigurationProperties("openapi.client")
    public AuthorizationCodeResourceDetails openapi() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    public OpenApiAuthorizedAspect openApiAuthorizedAspect() {
        final OpenApiAuthorizedAspect openApiAuthorizedAspect = new OpenApiAuthorizedAspect();
        openApiAuthorizedAspect.setRedirectUri(OPEN_API_URI);
        return openApiAuthorizedAspect;
    }
}