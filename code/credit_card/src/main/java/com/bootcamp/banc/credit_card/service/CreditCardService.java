package com.bootcamp.banc.credit_card.service;

import com.bootcamp.banc.credit_card.dto.CreditCardDto;
import com.bootcamp.banc.credit_card.dto.CreditCardMovDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Mono<CreditCardDto> contratarTarjeta(CreditCardDto creditCardDto);
    Mono<CreditCardDto> consultarTarjeta(String numeroTarjeta);
    Mono<CreditCardMovDto> operarTarjeta(String numeroTarjeta,CreditCardMovDto creditCardMovDto);
    Flux<CreditCardDto> listarTarjetas(String tipoDocumento, String numeroDocumento);
    Flux<CreditCardMovDto> listarMovimientos(String numeroTarjeta);

}
