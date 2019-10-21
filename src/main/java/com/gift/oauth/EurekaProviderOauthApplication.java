package com.gift.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories("com.gift.repository")
@EntityScan("com.gift.dao")
//@EnableResourceServer
public class EurekaProviderOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderOauthApplication.class, args);
	}

}
