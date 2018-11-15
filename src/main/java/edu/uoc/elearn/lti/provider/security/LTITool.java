package edu.uoc.elearn.lti.provider.security;

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
public class LTITool {
	private String name;
	private String clientId;
	private String keySetUrl;
	private String accessTokenUrl;
	private String privateKey;
	private String publicKey;
}
