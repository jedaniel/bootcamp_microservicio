package com.bootcamp.bank.current_account.repository;

import com.bootcamp.bank.current_account.entity.CurrentAccountMovEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CurrentAccountMovRepository extends ReactiveMongoRepository<CurrentAccountMovEntity,String> {
    Flux<CurrentAccountMovEntity> findByIdCurrentAccount(String idCurrentAccount);
}
