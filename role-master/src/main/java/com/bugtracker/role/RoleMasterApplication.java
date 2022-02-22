package com.bugtracker.role;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class RoleMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoleMasterApplication.class, args);
	}

}
