package com.bootcamp.bank.fixed_account.service.impl;

import com.bootcamp.bank.fixed_account.dto.FixedAccountDto;
import com.bootcamp.bank.fixed_account.dto.FixedAccountMovDto;
import com.bootcamp.bank.fixed_account.dto.ClientDto;
import com.bootcamp.bank.fixed_account.entity.FixedAccountEntity;
import com.bootcamp.bank.fixed_account.entity.FixedAccountMovEntity;
import com.bootcamp.bank.fixed_account.exception.CustomBusinessException;
import com.bootcamp.bank.fixed_account.mapper.FixedAccountMapper;
import com.bootcamp.bank.fixed_account.mapper.FixedAccountMovMapper;
import com.bootcamp.bank.fixed_account.repository.FixedAccountMovRepository;
import com.bootcamp.bank.fixed_account.repository.FixedAccountRepository;
import com.bootcamp.bank.fixed_account.service.FixedAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientRequest;

import java.time.Duration;

@Service
public class FixedAccountServiceImpl implements FixedAccountService {

    @Autowired
    FixedAccountRepository fixedAccountRepository;

    @Autowired
    FixedAccountMovRepository fixedAccountMovRepository;

    @Autowired
    FixedAccountMapper fixedAccountMapper;

    @Autowired
    FixedAccountMovMapper fixedAccountMovMapper;

    @Value("${uri.client}")
    String uri_client;

    @Override
    public Mono<FixedAccountDto> create(FixedAccountDto fixedAccountDto) {

        return WebClient.create()
//                .get()
//                .uri(uri_client + "/v1/client/exists/" + fixedAccountDto.getTipoDocumento() + "/" + fixedAccountDto.getNumeroDocumento())
//                .httpRequest(httpRequest -> {
//                    HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
//                    reactorRequest.responseTimeout(Duration.ofSeconds(2));
//                })
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .flatMap(c -> {
//                    if (c) {
//                        FixedAccountEntity fixedAccountEntity = fixedAccountMapper.toFixedAccountEntity(fixedAccountDto);
//                        fixedAccountEntity.setNumeroCuenta(java.util.UUID.randomUUID().toString());
//                        fixedAccountEntity.setCantidadMovimiento(1);
//                        fixedAccountEntity.setSaldoDisponible(fixedAccountEntity.getMontoDeposito());
//                        fixedAccountEntity.setFechaUltimoMovimiento(fixedAccountEntity.getFechaInicioVigencia());
//
//                        return fixedAccountRepository.save(fixedAccountEntity).flatMap(d -> {
//                            FixedAccountMovEntity fixedAccountMovEntity = new FixedAccountMovEntity();
//                            fixedAccountMovEntity.setFechaMovimiento(fixedAccountEntity.getFechaInicioVigencia());
//                            fixedAccountMovEntity.setTipoMovimiento("DEPOSITO");
//                            fixedAccountMovEntity.setMontoMovimiento(fixedAccountEntity.getMontoDeposito());
//                            fixedAccountMovEntity.setIdFixedAccount(d.getId());
//                            return fixedAccountMovRepository.save(fixedAccountMovEntity)
//                                    .flatMap(e -> {
//                                        d.setFixedAccountMovEntity(e);
//                                        return Mono.just(d);
//                                    });
//                        });
//                    } else {
//                        return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe para aperturar cuenta!!!"));
//                    }
//                }).flatMap(c -> {
//                    return Mono.just(fixedAccountMapper.toFixedAccountDto(c));
//                });
                .get()
                .uri(uri_client + "/v1/client/" + fixedAccountDto.getTipoDocumento() + "/" + fixedAccountDto.getNumeroDocumento())
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

                    if (c.getTipoCliente().equals("PERSONAL")) {
                        FixedAccountEntity fixedAccountEntity = fixedAccountMapper.toFixedAccountEntity(fixedAccountDto);
                        fixedAccountEntity.setNumeroCuenta(java.util.UUID.randomUUID().toString());
                        fixedAccountEntity.setCantidadMovimiento(1);
                        fixedAccountEntity.setSaldoDisponible(fixedAccountEntity.getMontoDeposito());
                        fixedAccountEntity.setFechaUltimoMovimiento(fixedAccountEntity.getFechaInicioVigencia());

                        return fixedAccountRepository.save(fixedAccountEntity).flatMap(d -> {
                            FixedAccountMovEntity fixedAccountMovEntity = new FixedAccountMovEntity();
                            fixedAccountMovEntity.setFechaMovimiento(fixedAccountEntity.getFechaInicioVigencia());
                            fixedAccountMovEntity.setTipoMovimiento("DEPOSITO");
                            fixedAccountMovEntity.setMontoMovimiento(fixedAccountEntity.getMontoDeposito());
                            fixedAccountMovEntity.setIdFixedAccount(d.getId());
                            return fixedAccountMovRepository.save(fixedAccountMovEntity)
                                    .flatMap(e -> {
                                        d.setFixedAccountMovEntity(e);
                                        return Mono.just(d);
                                    });
                        });
                    } else {
                        return Mono.error(() -> new CustomBusinessException("CLIENTE_NO_PERMITIDO", "Tipo de cliente no permitido para aperturar cuenta!!!"));
                    }
                }).flatMap(c -> {
                    return Mono.just(fixedAccountMapper.toFixedAccountDto(c));
                });
    }

    @Override
    public Mono<FixedAccountMovDto> realizarMovimiento(String numeroCuenta, FixedAccountMovDto fixedAccountMovDto) {
        return fixedAccountRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                }))
                .flatMap(c -> {
                    FixedAccountMovEntity fixedAccountMovEntity = fixedAccountMovMapper.toFixedAccountMovEntity(fixedAccountMovDto);
                    fixedAccountMovEntity.setIdFixedAccount(c.getId());
                    return fixedAccountMovRepository.save(fixedAccountMovEntity)
                            .flatMap(d -> {
                                //Actualizar el saldo según el tipo de movimiento
                                if (d.getTipoMovimiento().equals("DEPOSITO")) {
                                    c.setSaldoDisponible(c.getSaldoDisponible() + d.getMontoMovimiento());
                                } else if (d.getTipoMovimiento().equals("RETIRO")) {
                                    if (c.getSaldoDisponible() - d.getMontoMovimiento() < 0) {
                                        return Mono.error(() -> new CustomBusinessException("ERROR", "Saldo insuficiente!!!"));
                                    } else {
                                        c.setSaldoDisponible(c.getSaldoDisponible() - d.getMontoMovimiento());
                                    }
                                }
                                return fixedAccountRepository.save(c)
                                        .switchIfEmpty(Mono.defer(() -> {
                                            return Mono.error(() -> new CustomBusinessException("ERROR", "No se actualizó saldo!!!"));
                                        }))
                                        .flatMap(e -> Mono.just(fixedAccountMovMapper.toFixedAccountMovDto(d)));
                            });
                });
    }

    @Override
    public Mono<FixedAccountDto> consultarCuenta(String numeroCuenta) {
        return fixedAccountRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                })).flatMap(c -> Mono.just(fixedAccountMapper.toFixedAccountDto(c)));
    }

    @Override
    public Flux<FixedAccountDto> listarCuenta(String tipoDocumento, String numeroDocumento) {
        return fixedAccountRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .switchIfEmpty(Flux.defer(() -> {
                    return Flux.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe!!!"));
                })).flatMap(c -> {
                    c.setNumeroDocumento(null);
                    c.setTipoDocumento(null);
                    c.setFechaInicioVigencia(null);
                    c.setFechaUltimoMovimiento(null);
                    c.setFechaFinVigencia(null);
                    return Flux.just(fixedAccountMapper.toFixedAccountDto(c));
                });
    }

    @Override
    public Flux<FixedAccountMovDto> consultarMovimiento(String numeroCuenta) {
        return fixedAccountRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                })).flux()
                .flatMap(c -> {
                    return fixedAccountMovRepository.findByIdFixedAccount(c.getId()).flatMap(d -> Flux.just(fixedAccountMovMapper.toFixedAccountMovDto(d)));
                });

    }
}

