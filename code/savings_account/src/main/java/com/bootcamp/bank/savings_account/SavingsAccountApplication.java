package com.bootcamp.bank.savings_account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SavingsAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavingsAccountApplication.class, args);
	}

}
