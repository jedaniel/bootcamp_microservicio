package com.bootcamp.bank.credit.repository;

import com.bootcamp.bank.credit.entity.CreditMovEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditMovRepository extends ReactiveMongoRepository<CreditMovEntity, String> {
    Flux<CreditMovEntity> findByIdCredit(String idCredit);
}
