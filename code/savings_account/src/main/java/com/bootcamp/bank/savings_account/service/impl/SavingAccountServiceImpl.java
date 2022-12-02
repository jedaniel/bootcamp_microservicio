package com.bootcamp.bank.savings_account.service.impl;

import com.bootcamp.bank.savings_account.dto.ClientDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountMovDto;
import com.bootcamp.bank.savings_account.entity.SavingAccountEntity;
import com.bootcamp.bank.savings_account.entity.SavingAccountMovEntity;
import com.bootcamp.bank.savings_account.exception.CustomBusinessException;
import com.bootcamp.bank.savings_account.mapper.SavingAccountMapper;
import com.bootcamp.bank.savings_account.mapper.SavingAccountMovMapper;
import com.bootcamp.bank.savings_account.repository.SavingAccountMovRepository;
import com.bootcamp.bank.savings_account.repository.SavingAccountRepository;
import com.bootcamp.bank.savings_account.service.SavingAccountService;
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
public class SavingAccountServiceImpl implements SavingAccountService {

    @Autowired
    SavingAccountRepository savingAccountRepository;

    @Autowired
    SavingAccountMovRepository savingAccountMovRepository;

    @Autowired
    SavingAccountMapper savingAccountMapper;

    @Autowired
    SavingAccountMovMapper savingAccountMovMapper;

    @Value("${uri.client}")
    String uri_client;

    @Value("${movimiento.maximo}")
    String movimientoMaximo;

    @Override
    public Mono<SavingAccountDto> crearCuenta(SavingAccountDto savingAccountDto) {
        return WebClient.create()
                .get()
                .uri(uri_client + "/v1/client/" + savingAccountDto.getTipoDocumento() + "/" + savingAccountDto.getNumeroDocumento())
                .httpRequest(httpRequest -> {
                    HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
                    reactorRequest.responseTimeout(Duration.ofSeconds(2));
                })
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe para aperturar cuenta!!!"));
                })
                .bodyToMono(ClientDto.class)
                .flatMap(a -> {
                    if (a.getTipoCliente().equals("PERSONAL")) {
                        return savingAccountRepository.existsByTipoDocumentoAndNumeroDocumentoAndEstado(savingAccountDto.getTipoDocumento(), savingAccountDto.getNumeroDocumento(), "ACTIVO")
                                .flatMap(c -> {
                                    if (!c) {
                                        SavingAccountEntity savingAccountEntity = savingAccountMapper.toSavingAccountEntity(savingAccountDto);
                                        savingAccountEntity.setNumeroCuenta(java.util.UUID.randomUUID().toString());
                                        savingAccountEntity.setSaldoDisponible(0.00);
                                        savingAccountEntity.setCantidadMovActual(0);
                                        savingAccountEntity.setCantidadMovPermitido(Integer.parseInt(movimientoMaximo));
                                        savingAccountEntity.setEstado("ACTIVO");
                                        return savingAccountRepository.save(savingAccountEntity)
                                                .flatMap(d -> Mono.just(savingAccountMapper.toSavingAccountDto(d)));
                                    } else {
                                        return Mono.error(() -> new CustomBusinessException("ERROR", "El cliente ya tiene una cuenta de ahorro!!!"));
                                    }
                                });
                    } else {
                        return Mono.error(() -> new CustomBusinessException("CLIENTE_NO_PERMITIDO", "Tipo de cliente no permitido para aperturar cuenta!!!"));
                    }
                });
    }

    @Override
    public Mono<SavingAccountMovDto> realizarTransaccion(String numeroCuenta, SavingAccountMovDto savingAccountMovDto) {
        return savingAccountRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                }))
                .flatMap(c -> {
                    if(c.getCantidadMovActual()>= c.getCantidadMovPermitido()){
                        return Mono.error(() -> new CustomBusinessException("TRANSACTION_NOT_PERMITTED", "No se permite más movimientos!!!"));
                    }
                    SavingAccountMovEntity savingAccountMovEntity = savingAccountMovMapper.toSavingAccountMovEntity(savingAccountMovDto);
                    savingAccountMovEntity.setIdSavingAccount(c.getId());
                    return savingAccountMovRepository.save(savingAccountMovEntity)
                            .flatMap(d -> {
                                c.setCantidadMovActual(c.getCantidadMovActual() + 1);
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
                                return savingAccountRepository.save(c)
                                        .switchIfEmpty(Mono.defer(() -> {
                                            return Mono.error(() -> new CustomBusinessException("ERROR", "No se actualizó saldo!!!"));
                                        }))
                                        .flatMap(e -> Mono.just(savingAccountMovMapper.toSavingAccountMovDto(d)));
                            });
                });
    }

    @Override
    public Mono<SavingAccountDto> consultarCuenta(String numeroCuenta) {
        return savingAccountRepository.findByNumeroCuenta(numeroCuenta)
                .flatMap(c -> Mono.just(savingAccountMapper.toSavingAccountDto(c)))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "La cuenta no existe!!!"));
                }));
    }

    @Override
    public Flux<SavingAccountDto> listarCuenta(String tipoDocumento, String numeroDocumento) {
        return savingAccountRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .flatMap(c -> Flux.just(savingAccountMapper.toSavingAccountDto(c)))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe!!!"));
                }));
    }

    @Override
    public Flux<SavingAccountMovDto> consultarMovimiento(String numeroCuenta) {
        return savingAccountRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe!!!"));
                }))
                .flux()
                .flatMap(c -> {
                    return savingAccountMovRepository.findByIdSavingAccount(c.getId())
                            .flatMap(d -> Flux.just(savingAccountMovMapper.toSavingAccountMovDto(d)));
                });
    }
}
