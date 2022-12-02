package com.bootcamp.bank.client.repository;

import com.bootcamp.bank.client.entity.ClientEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<ClientEntity, String> {
    Mono<ClientEntity> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    Mono<Boolean> existsByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
