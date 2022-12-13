package com.bootcamp.bank.credit.repository;

import com.bootcamp.bank.credit.entity.CreditEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<CreditEntity, String> {
    Flux<CreditEntity> findByTipoDocumentoAndNumeroDocumentoAndEstado(String tipoDocumento, String numeroDocumento, String estado);
    Mono<CreditEntity> findByNumeroCreditoAndEstado(String numeroCredito, String estado);
}
