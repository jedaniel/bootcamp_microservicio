package com.bootcamp.bank.current_account.repository;

import com.bootcamp.bank.current_account.entity.CurrentAccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CurrentAccountRepository extends ReactiveMongoRepository<CurrentAccountEntity, String> {
    Flux<CurrentAccountEntity> findByTipoDocumentoAndNumeroDocumentoAndEstado(String tipoDocumento, String numeroDocumento, String estado);
    Mono<Boolean> existsByTipoCuentaAndTipoDocumentoAndNumeroDocumentoAndEstado(String tipoCuenta, String tipoDocumento, String numeroDocumento, String estado);
    Mono<Boolean> existsByNumeroCuentaAndEstado(String numeroCuenta, String estado);
    Mono<CurrentAccountEntity> findByNumeroCuentaAndEstado(String numeroCuenta, String estado);
}
