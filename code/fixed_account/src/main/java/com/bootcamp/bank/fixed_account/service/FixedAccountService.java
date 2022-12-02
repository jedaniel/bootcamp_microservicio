package com.bootcamp.bank.fixed_account.service;

import com.bootcamp.bank.fixed_account.dto.FixedAccountDto;
import com.bootcamp.bank.fixed_account.dto.FixedAccountMovDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FixedAccountService {
    Mono<FixedAccountDto> create(FixedAccountDto fixedAccountDto);
    Mono<FixedAccountMovDto> realizarMovimiento(String numeroCuenta, FixedAccountMovDto fixedAccountMovDto);
    Mono<FixedAccountDto> consultarCuenta(String numeroCuenta);
    Flux<FixedAccountDto> listarCuenta(String tipoDocumento, String numeroDocumento);
    Flux<FixedAccountMovDto> consultarMovimiento(String numeroCuenta);
}
