package com.amalvadkar.lms.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class LmsServiceAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsServiceAuthApplication.class, args);
	}

}
