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
@Validated
public class KeySetBean {
	@NotEmpty
	private String id;

	@NotEmpty
	private List<KeyBean> keys;
}
