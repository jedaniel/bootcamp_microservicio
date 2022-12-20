package com.bootcamp.bank.savings_account.service;

import com.bootcamp.bank.savings_account.dto.SavingAccountDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountMovDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountTransferenciaDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SavingAccountService {
    Mono<SavingAccountDto> crearCuenta(SavingAccountDto savingAccountDto);
    Mono<List<SavingAccountMovDto>> realizarTransaccion(String numeroCuenta, SavingAccountMovDto savingAccountMovDto);
    Mono<SavingAccountDto> consultarCuenta(String numeroCuenta);
    Flux<SavingAccountDto> listarCuenta(String tipoDocumento, String numeroDocumento);
    Flux<SavingAccountMovDto> consultarMovimiento(String numeroCuenta);
    Mono<SavingAccountMovDto> realizarTransferencia(SavingAccountTransferenciaDto savingAccountTransferenciaDto);
}
