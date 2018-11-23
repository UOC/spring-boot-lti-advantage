package edu.uoc.elearn.spring.security.lti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToolDefinition {
	private String name;
	private String clientId;
	private String keySetUrl;
	private String accessTokenUrl;
	private String privateKey;
	private String publicKey;
}
