package edu.uoc.elc.spring.lti.tool.registration;

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
public class DeploymentBean {
	@NotEmpty
	private String deploymentId;
}
