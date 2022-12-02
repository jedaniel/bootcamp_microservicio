package com.bootcamp.bank.fixed_account.repository;

import com.bootcamp.bank.fixed_account.entity.FixedAccountMovEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FixedAccountMovRepository extends ReactiveMongoRepository<FixedAccountMovEntity, String> {
    Flux<FixedAccountMovEntity> findByIdFixedAccount(String idFixedAccount);
}
