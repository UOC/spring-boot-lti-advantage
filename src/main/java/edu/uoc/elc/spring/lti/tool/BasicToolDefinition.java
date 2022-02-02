package edu.uoc.elc.spring.lti.tool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "lti")
@Validated
public class BasicToolDefinition {
	@NotEmpty
	private String name;
	@NotEmpty
	private String clientId;
	@NotEmpty
	private String keySetUrl;
	@NotEmpty
	private String platform;
	@NotEmpty
	private List<String> deploymentIds;
	@NotEmpty
	private String accessTokenUrl;
	@NotEmpty
	private String oidcAuthUrl;
	@NotEmpty
	private String privateKey;
	@NotEmpty
	private String publicKey;

}
