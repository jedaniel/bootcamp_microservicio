package com.bootcamp.banc.credit_card.repository;

import com.bootcamp.banc.credit_card.entity.CreditCardEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCardEntity, String> {
    Mono<CreditCardEntity> findByNumeroTarjetaAndEstado(String numeroTarjeta, String estado);
    Flux<CreditCardEntity> findByTipoDocumentoAndNumeroDocumentoAndEstado(String tipoDocumento, String numeroDocumento, String estado);
}
