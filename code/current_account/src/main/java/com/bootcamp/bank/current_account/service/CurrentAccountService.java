package com.bootcamp.bank.current_account.service;

import com.bootcamp.bank.current_account.dto.CurrentAccountDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountMovDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrentAccountService {
    Mono<CurrentAccountDto> save(CurrentAccountDto currentAccountDto);
    Mono<CurrentAccountMovDto> realizarMovimiento (String numeroCuenta, CurrentAccountMovDto currentAccountMovDto);
    Flux<CurrentAccountMovDto> listarMovimiento (String numeroCuenta);
    Flux<CurrentAccountDto> listarCurrentAccount(String tipoDocumento, String numeroDocumento);
    Mono<CurrentAccountDto> buscarCurrentAccount(String numeroCuenta);
}
