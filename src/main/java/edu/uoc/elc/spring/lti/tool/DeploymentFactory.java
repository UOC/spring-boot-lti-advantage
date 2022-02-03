package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Deployment;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public class DeploymentFactory {
	public Deployment from(DeploymentBean bean) {
		return Deployment.builder()
						.deploymentId(bean.getDeploymentId())
						.build();
	}
}
