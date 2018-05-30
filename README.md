# Spring Boot LTI Provider
This is an skeleton of LTI Provider for Spring Boot. We test it with SpringBoot 1.2.5.RELEASE


## Consumer Keys and secrets
To use the LTI provider you have to enable the allowed consumer keys and secrets into file src/main/resources/authorizedConsumersKey.properties

This is a configuration to be readed by providers to authorize the consumer key and gets the secret
```
consumer_key."name_consumer".enabled=1
consumer_key."name_consumer".secret=secret
consumer_key."name_consumer".callBackUrl=
consumer_key."name_consumer".fieldSessionId=token
```



## Compile
To compile you will need to add to your local repository these 2 files

```
mvn install:install-file -Dfile=PATH_TO_SpringBootLTIProvider/lib/JavaUtils-1.1.2.jar -DgroupId=org.campusproject -DartifactId=JavaUtils -Dversion=1.1.2 -Dpackaging=jar
mvn install:install-file -Dfile=PATH_TO_SpringBootLTIProvider/lib/lti-1.0.6-SNAPSHOT.jar -DgroupId=edu.uoc -DartifactId=lti -Dversion=1.0.6-SNAPSHOT -Dpackaging=jar

```

You have to change PATH_TO_SpringBootLTIProvider for your absolute path to SpringBootLTIProvider
