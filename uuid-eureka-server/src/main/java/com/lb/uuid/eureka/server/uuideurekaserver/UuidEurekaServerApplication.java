package com.lb.uuid.eureka.server.uuideurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class UuidEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UuidEurekaServerApplication.class, args);
	}

}
