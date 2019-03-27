package edu.uoc.elearn;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("edu.uoc.elearn.spring.security.lti.tool")
@EnableConfigurationProperties
@PropertySource("application.properties")
public class Config {

}
