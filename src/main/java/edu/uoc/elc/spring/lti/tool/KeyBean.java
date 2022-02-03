package edu.uoc.elc.spring.lti.tool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
@Component
@Validated
public class KeyBean {
	@NotEmpty
	private String id;
	@NotEmpty
	private String privateKey;
	@NotEmpty
	private String publicKey;
	@NotEmpty
	private String algorithm;
}
