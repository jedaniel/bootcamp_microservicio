package com.bootcamp.bank.credit.service;

import com.bootcamp.bank.credit.dto.CreditDto;
import com.bootcamp.bank.credit.dto.CreditMovDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<CreditDto> obtenerCredito(CreditDto creditDto);
    Mono<CreditDto> buscarCredito(String numeroCredito);
    Flux<CreditDto> listarCreditos(String tipoDocumento, String numeroDocumento);
    Mono<CreditMovDto> realizarTransaccionCredito(String numeroCredito, CreditMovDto creditMovDto);
    Flux<CreditMovDto> listarMovimientoCredito(String numeroCredito);
}
