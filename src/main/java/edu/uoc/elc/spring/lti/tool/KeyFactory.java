package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.Key;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public class KeyFactory {
	public Key from(KeyBean bean) {
		return Key.builder()
						.id(bean.getId())
						.algorithm(bean.getAlgorithm())
						.publicKey(bean.getPublicKey())
						.privateKey(bean.getPrivateKey())
						.build();
	}
}
