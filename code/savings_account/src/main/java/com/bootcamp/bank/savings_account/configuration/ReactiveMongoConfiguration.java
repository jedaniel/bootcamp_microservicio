package com.bootcamp.bank.savings_account.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class ReactiveMongoConfiguration  {

    @Bean
    ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory rdbf)  {
        return new ReactiveMongoTransactionManager(rdbf);
    }

    @Bean
    TransactionalOperator transactionOperator(ReactiveTransactionManager rtm) {
        return TransactionalOperator.create(rtm);
    }

}
