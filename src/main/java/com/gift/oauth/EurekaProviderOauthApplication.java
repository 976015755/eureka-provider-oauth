package com.gift.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.gift.feign")
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories("com.gift.repository")
@EntityScan("com.gift.dao")
@EnableZuulProxy
//@EnableResourceServer
public class EurekaProviderOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderOauthApplication.class, args);
	}

}
