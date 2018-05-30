# OpenAPI + Spring Boot LTI Provider
There's a support for OpenAPI in this project. You can access OpenApi's `access token` and make authenticated calls to OpenAPI resources right inside a LTI SpringBoot Security powered application.

This project has a fully functional implementation and can be used as an example

# Dependency

The support relies in Spring AOP and Oauth2. Just make sure there's these dependencies in your `pom.xml`

        <dependency>
            <groupId>org.springframework.security.oauth</groupId>
            <artifactId>spring-security-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

See `pom.xml` in this project

# Configuration

In your ApplicationSecurity class:

1. Mark your Application with the annotations:

    `@EnableOAuth2Client` to allow oauth2 client calls  
    `@EnableAspectJAutoProxy` to allow Advices in the app
    
2. Define a `OAuth2ClientContext oauth2ClientContext`. This will be injected by Spring Security thanks to `@EnableOAuth2Client`:

    	@Autowired
    	OAuth2ClientContext oauth2ClientContext;
    
3. Define the filter for redirecting to OpenAPI:

        private OpenApiClientFilter openapiFilter() {
            OpenApiClientFilter openApiFilter = new OpenApiClientFilter(OPEN_API_URI);
            openApiFilter.setRestTemplate(openApiRestTemplate());
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
        public OAuth2RestOperations openApiRestTemplate() {
            return new OAuth2RestTemplate(openapi(), oauth2ClientContext);
        }
    

4. Add the filter to your filter chain:

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
            ... // your configuration
            .and().addFilterBefore(openapiFilter(), BasicAuthenticationFilter.class)
            ...// your configuration
        }

5. Define the advice bean:
    
        @Bean
        public OpenApiAuthorizedAspect openApiAuthorizedAspect() {
            final OpenApiAuthorizedAspect openApiAuthorizedAspect = new OpenApiAuthorizedAspect();
            openApiAuthorizedAspect.setRedirectUri("/login/openapi");
            return openApiAuthorizedAspect;
        }
    
    The URI in `openApiAuthorizedAspect.setRedirectUri` **MUST be the same** as the uri of the `OpenApiClientFilter`.       	
 
See `edu.uoc.elearn.lti.provider.config.ApplicationSecurity.java` in this project
   
# Open API client configuration

Add to your `application.properties` the configuration for OpenAPI:

    # Open API Client Configuration
    openapi.client.accessTokenUri=https://cv.uoc.edu/webapps/uocapi/oauth/token
    openapi.client.userAuthorizationUri=https://cv.uoc.edu/webapps/uocapi/oauth/authorize
    openapi.client.tokenName=access_token
    openapi.client.clientAuthenticationScheme=form
    openapi.client.authenticationScheme=query
    openapi.client.scope=READ READ_GRADEBOOK


Adapt the scope's to your application's needs.

Adapt the URL's as well if you are targeting others environments.

See `application.properties` in this project

## Client id and secret

We store client id and secret in a configuration file (typically `application-{devel|pro}.yml`):


    # Open Api client
    openapi:
      client:
        clientId: "your_client_id"
        clientSecret: "your_secret"

See `application.properties` in this project

# PreAuth annotation

The annotation `edu.uoc.elearn.openapi.spring.OpenApiAuthorized` defines methods to be called only when the application has been authorized by the user in OpenApi. 

If the application hasn't been authorized (and the application has been configured the *filter* properly), the application redirects itself to the authorization process in OpenAPI

See `edu.uoc.elearn.lti.provider.controller.UserController.java` in this project

# Save request

If you want OpenApi to redirect exactly where you were, just add a parameter `HttpServletRequest request` to your `edu.uoc.elearn.openapi.spring.OpenApiAuthorized`'s annotated method  

See `edu.uoc.elearn.lti.provider.controller.UserController.java` in this project

# Calls to OpenApi

## Define urls
Add urls in `application.properties`:

    openapi.client.resource.user=https://cv.uoc.edu/webapps/uocapi/api/v1/user

## Call to resources

1. Add beans in your controller:

        @Autowired
        OAuth2RestOperations openApiRestTemplate;
    
        @Value("${openapi.client.resource.user}")
        private String userResourceUrl;
 
2. Make the call:
    
        Map userData = openApiRestTemplate.getForObject(userResourceUrl, Map.class);
                
See `edu.uoc.elearn.lti.provider.controller.UserController.java` in this project

> We should definitely improve this, making a client library with objects 
