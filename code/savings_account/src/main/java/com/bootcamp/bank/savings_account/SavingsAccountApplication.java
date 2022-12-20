package com.bootcamp.bank.savings_account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
public class SavingsAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavingsAccountApplication.class, args);
	}

}
