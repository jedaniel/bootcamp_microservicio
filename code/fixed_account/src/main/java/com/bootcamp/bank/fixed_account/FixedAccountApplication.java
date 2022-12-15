package com.bootcamp.bank.fixed_account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FixedAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(FixedAccountApplication.class, args);
	}

}
