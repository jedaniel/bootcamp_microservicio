package com.bootcamp.bank.current_account.service.impl;

import com.bootcamp.bank.current_account.dto.ClientDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountMovDto;
import com.bootcamp.bank.current_account.exception.CustomBusinessException;
import com.bootcamp.bank.current_account.mapper.CurrentAccountMapper;
import com.bootcamp.bank.current_account.mapper.CurrentAccountMovMapper;
import com.bootcamp.bank.current_account.repository.CurrentAccountMovRepository;
import com.bootcamp.bank.current_account.repository.CurrentAccountRepository;
import com.bootcamp.bank.current_account.service.CurrentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientRequest;

import java.time.Duration;

@Service
public class CurrentAccountServiceImpl implements CurrentAccountService {

    @Autowired
    CurrentAccountRepository currentAccountRepository;

    @Autowired
    CurrentAccountMovRepository currentAccountMovRepository;

    @Autowired
    CurrentAccountMapper currentAccountMapper;

    @Autowired
    CurrentAccountMovMapper currentAccountMovMapper;


    @Value("${uri.client}")
    String uri_client;

    @Override
    public Mono<CurrentAccountDto> save(CurrentAccountDto currentAccountDto) {
        return WebClient.create()
                .get()
                .uri(uri_client + "/v1/client/" + currentAccountDto.getTipoDocumento() + "/" + currentAccountDto.getNumeroDocumento())
                .httpRequest(httpRequest -> {
                    HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
                    reactorRequest.responseTimeout(Duration.ofSeconds(2));
                })
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe para aperturar cuenta!!!"));
                })
                .bodyToMono(ClientDto.class)
                .flatMap(c -> {
                    currentAccountDto.setNumeroCuenta(java.util.UUID.randomUUID().toString());
                    if (c.getTipoCliente().equals("PERSONAL")) {
                        return currentAccountRepository.existsByTipoCuentaAndTipoDocumentoAndNumeroDocumentoAndEstado(currentAccountDto.getTipoCuenta()
                                        , currentAccountDto.getTipoDocumento(), currentAccountDto.getNumeroDocumento(), "ACTIVO")
                                .flatMap(b -> {
                                    if (!b) {
                                        return currentAccountRepository.save(currentAccountMapper.toCurrentAccountEntity(currentAccountDto))
                                                .flatMap(d -> Mono.just(currentAccountMapper.toCurrentAccountDto(d)));
                                    } else {
                                        return Mono.error(() -> new CustomBusinessException("APERTURA_NO_PERMITIDO", "El cliente ya tiene una cuenta corriente activa!!!"));
                                    }
                                });
                    } else {
                        return currentAccountRepository.save(currentAccountMapper.toCurrentAccountEntity(currentAccountDto))
                                .flatMap(d -> Mono.just(currentAccountMapper.toCurrentAccountDto(d)));
                    }
                });

    }

    @Override
    @Transactional
    public Mono<CurrentAccountMovDto> realizarMovimiento(String numeroCuenta, CurrentAccountMovDto currentAccountMovDto) {
        return currentAccountRepository.findByNumeroCuentaAndEstado(numeroCuenta, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                }))
                .flatMap(c -> {
                    currentAccountMovDto.setIdCurrentAccount(c.getId());
                    if (currentAccountMovDto.getTipoMovimiento().equals("DEPOSITO")) {
                        c.setSaldoDisponible(c.getSaldoDisponible() + currentAccountMovDto.getMontoMovimiento());
                    } else if (currentAccountMovDto.getTipoMovimiento().equals("RETIRO")) {
                        if (c.getSaldoDisponible() - currentAccountMovDto.getMontoMovimiento() < 0) {
                            return Mono.error(() -> new CustomBusinessException("MOVIMIENTO_NO_PERMITIDO", "Saldo no disponible!!!"));
                        } else {
                            c.setSaldoDisponible(c.getSaldoDisponible() - currentAccountMovDto.getMontoMovimiento());
                        }
                    } else {
                        return Mono.error(() -> new CustomBusinessException("MOVIMIENTO_NO_PERMITIDO", "Tipo de movimiento no permitido!!!"));
                    }
                    return currentAccountMovRepository.save(currentAccountMovMapper.toCurrentAccountMovEntity(currentAccountMovDto))
                            .flatMap(d -> {
                                return currentAccountRepository.save(c)
                                        .flatMap(e -> Mono.just(currentAccountMovMapper.toCurrentAccountMovDto(d)));

                            });
                });
    }

    @Override
    public Flux<CurrentAccountMovDto> listarMovimiento(String numeroCuenta) {
        return currentAccountRepository.findByNumeroCuentaAndEstado(numeroCuenta, "ACTIVO")
                .flux()
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                }))
                .flatMap(c -> {
                    return currentAccountMovRepository.findByIdCurrentAccount(c.getId())
                            .flatMap(d -> Flux.just(currentAccountMovMapper.toCurrentAccountMovDto(d)));
                });
    }

    @Override
    public Flux<CurrentAccountDto> listarCurrentAccount(String tipoDocumento, String numeroDocumento) {
        return currentAccountRepository.findByTipoDocumentoAndNumeroDocumentoAndEstado(tipoDocumento, numeroDocumento, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "El cliente no tiene cuentas!!!"));
                }))
                .flatMap(c -> Flux.just(currentAccountMapper.toCurrentAccountDto(c)));
    }

    @Override
    public Mono<CurrentAccountDto> buscarCurrentAccount(String numeroCuenta) {
        return currentAccountRepository.findByNumeroCuentaAndEstado(numeroCuenta, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                }))
                .flatMap(c -> Mono.just(currentAccountMapper.toCurrentAccountDto(c)));
    }
}
