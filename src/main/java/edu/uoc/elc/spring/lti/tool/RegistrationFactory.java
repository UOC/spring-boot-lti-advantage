package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Registration;

import java.util.stream.Collectors;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public class RegistrationFactory {
	public Registration from(RegistrationBean bean) {
		KeySetFactory keySetFactory = new KeySetFactory();
		DeploymentFactory deploymentFactory = new DeploymentFactory();
		return Registration.builder()
						.id(bean.getId())
						.clientId(bean.getClientId())
						.name(bean.getName())
						.platform(bean.getPlatform())
						.keySetUrl(bean.getKeySetUrl())
						.accessTokenUrl(bean.getAccessTokenUrl())
						.oidcAuthUrl(bean.getOidcAuthUrl())
						.keySet(keySetFactory.from(bean.getKeySet()))
						.deployments(bean.getDeployments().stream().map(deploymentFactory::from).collect(Collectors.toList()))
						.build();
	}
}
