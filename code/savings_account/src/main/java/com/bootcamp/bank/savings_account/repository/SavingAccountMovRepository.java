package com.bootcamp.bank.savings_account.repository;

import com.bootcamp.bank.savings_account.entity.SavingAccountMovEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface SavingAccountMovRepository extends ReactiveMongoRepository<SavingAccountMovEntity, String> {
    Flux<SavingAccountMovEntity> findByIdSavingAccount(String idSavingAccount);
    Flux<SavingAccountMovEntity> findByIdSavingAccountAndFechaMovimientoGreaterThan(String idSavingAccount, LocalDate fechaMovimiento);
}
