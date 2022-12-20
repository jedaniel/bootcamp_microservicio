package com.bootcamp.bank.savings_account.repository;

import com.bootcamp.bank.savings_account.entity.SavingAccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SavingAccountRepository extends ReactiveMongoRepository<SavingAccountEntity, String> {
    Mono<Boolean> existsByTipoDocumentoAndNumeroDocumentoAndEstado(String tipoDocumento, String numeroDocumento, String estado);
    Mono<SavingAccountEntity> findByNumeroCuenta(String numeroCuenta);
    Flux<SavingAccountEntity> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
