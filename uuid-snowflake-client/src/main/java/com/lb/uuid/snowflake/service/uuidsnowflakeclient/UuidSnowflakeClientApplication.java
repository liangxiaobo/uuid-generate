package com.lb.uuid.snowflake.service.uuidsnowflakeclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UuidSnowflakeClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(UuidSnowflakeClientApplication.class, args);
    }

}
