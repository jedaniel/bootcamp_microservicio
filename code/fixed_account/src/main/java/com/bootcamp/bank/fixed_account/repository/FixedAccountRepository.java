package com.bootcamp.bank.fixed_account.repository;

import com.bootcamp.bank.fixed_account.entity.FixedAccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FixedAccountRepository extends ReactiveMongoRepository<FixedAccountEntity, String> {
    Mono<FixedAccountEntity> findByNumeroCuenta(String numeroCuenta);
    Flux<FixedAccountEntity> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
