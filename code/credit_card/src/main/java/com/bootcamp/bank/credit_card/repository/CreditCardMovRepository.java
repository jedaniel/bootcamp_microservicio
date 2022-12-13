package com.bootcamp.bank.credit_card.repository;

import com.bootcamp.bank.credit_card.entity.CreditCardMovEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditCardMovRepository extends ReactiveMongoRepository<CreditCardMovEntity, String> {
    Flux<CreditCardMovEntity> findByIdCreditCard(String idCreditoCard);
}
