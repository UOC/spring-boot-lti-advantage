package edu.uoc.elc.spring.lti.tool;

import edu.uoc.elc.lti.tool.KeySet;

import java.util.stream.Collectors;

/**
 * Universitat Oberta de Catalunya
 * Made for the project spring-boot-lti-advantage
 */
public class KeySetFactory {
	public KeySet from(KeySetBean bean) {
		KeyFactory keyFactory = new KeyFactory();
		return KeySet.builder()
						.id(bean.getId())
						.keys(bean.getKeys().stream().map(keyFactory::from).collect(Collectors.toList()))
						.build();
	}
}
