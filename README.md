# Spring Boot LTI Advantage library

LTI Advantage library for Spring Boot projects. Adds LTI Tool capabilities to your SpringBoot web application

See https://github.com/UOC/java-lti-1.3-provider-example for an example.

## Usage

1. Set your maven installation to work with Github packages, following https://help.github.com/en/packages/using-github-packages-with-your-projects-ecosystem/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages

2. Add the dependency to your `pom.xml` file:

```xml
  <dependency>
	<groupId>edu.uoc.elc.lti</groupId>
	<artifactId>spring-boot-lti-advantage</artifactId>
	<version>0.0.6</version>
  </dependency>
```  


## Installation

This library depends on the following additional libraries:

* [LTI 1.3 core](https://github.com/UOC/java-lti-1.3-core)
* [Java-lti-1.3](https://github.com/UOC/java-lti-1.3)
* [LTI 1.3 JWT](https://github.com/UOC/java-lti-1.3-jwt) for testing

Install it using maven:

```bash
./mvnw install
```

## About

This library makes essentially two things: Configures SpringBoot Security for validating using the [Java-lti-1.3](https://github.com/UOC/java-lti-1.3) 
library and implements some [LTI 1.3 core](https://github.com/UOC/java-lti-1.3-core#about) using SpringBoot.

### SpringBoot Security (AKA making your webapp a LTI Advantage Tool)

The configuration of the SpringBoot Security framework is made in the `edu.uoc.elc.spring.lti.security.LTIApplicationSecurity` class.
It adds the `edu.uoc.elc.spring.lti.security.LTIProcessingFilter` as a filter if your application's filter chain, creating the principal 
and credentials when the launch is a valid LTI 1.3 launch.

It also adds the `edu.uoc.elc.spring.lti.security.openid.OIDCFilter` for managing OIDC launches.

### Utilities

This library contains several utility classes for your application:

* `edu.uoc.elc.spring.lti.tool.ToolProvider` is the basic class. It provides your application to all the
capabilities of the tool.

* `edu.uoc.elc.spring.lti.security.User` contains information about the current LTI user.

* `edu.uoc.elc.spring.lti.security.Context` contains information about the current LTI context.

These utility classes can be injected directly in your controllers or methods:

```java
  public ModelAndView init(User user, Context context, ToolProvider toolProvider) {
``` 

### Locale

The library adds the `edu.uoc.elc.spring.lti.security.interceptors.LTILocaleChangeInterceptor` interceptor for
setting the Locale of the webapp to the Locale of the LTI launch. If you need the locale in your app you can simply 
add a `Locale locale` param in your controller method.

### Implementations of LTI 1.3 core interfaces

This library contains several implementations of LTI 1.3 core interfaces:

* `edu.uoc.elc.spring.lti.security.openid.HttpSessionOIDCLaunchSession` for managing OIDC Launch 
state in the HTTP Session.

* `edu.uoc.elc.spring.lti.ags.RestTemplateLineItemServiceClient` for Assignment and Grades Service's Line Item service

* `edu.uoc.elc.spring.lti.ags.RestTemplateResultServiceClient` for Assignment and Grades Service's Results service

* `edu.uoc.elc.spring.lti.ags.RestTemplateScoreServiceClient` for Assignment and Grades Service's Score publish service

These Assignment and Grades services are accessed through the `edu.uoc.elc.spring.lti.tool.ToolProvider` bean

### Configuration

Configuration of the tool is made in the `edu.uoc.elc.spring.lti.tool.ToolDefinitionBean` class. Also, remaining 
LTI Core's interfaces are injected as beans. See the test class `edu.uoc.elc.Config` for an example 

## Contributing

Thanks for being interested in this project. The way of contributing is the common for almost all projects:

1. Fork the project to your account
2. Implement your changes
3. Make a pull request

If you need further information contact to `xaracil at uoc dot edu`
