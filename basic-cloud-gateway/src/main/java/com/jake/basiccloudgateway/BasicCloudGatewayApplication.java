package com.jake.basiccloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;

@SpringBootApplication
public class BasicCloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicCloudGatewayApplication.class, args);
	}

}
