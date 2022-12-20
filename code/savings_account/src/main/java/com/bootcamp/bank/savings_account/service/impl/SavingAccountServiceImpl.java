package com.bootcamp.bank.savings_account.service.impl;

import com.bootcamp.bank.savings_account.dto.ClientDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountMovDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountTransferenciaDto;
import com.bootcamp.bank.savings_account.entity.SavingAccountEntity;
import com.bootcamp.bank.savings_account.entity.SavingAccountMovEntity;
import com.bootcamp.bank.savings_account.exception.CustomBusinessException;
import com.bootcamp.bank.savings_account.mapper.SavingAccountMapper;
import com.bootcamp.bank.savings_account.mapper.SavingAccountMovMapper;
import com.bootcamp.bank.savings_account.repository.SavingAccountMovRepository;
import com.bootcamp.bank.savings_account.repository.SavingAccountRepository;
import com.bootcamp.bank.savings_account.service.SavingAccountService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

//    @Autowired
//    TransactionalOperator transactionalOperator;

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
    @Transactional
    public Mono<List<SavingAccountMovDto>> realizarTransaccion(String numeroCuenta, SavingAccountMovDto savingAccountMovDto) {
        return savingAccountRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta no existe!!!"));
                }))
                .flatMap(c -> {
                    LocalDate date = LocalDate.now();
                    double comision = c.getComision();
                    //Obtener movimientos del mes en curso
                    return savingAccountMovRepository.findByIdSavingAccountAndFechaMovimientoGreaterThan(c.getId(), date.minusDays(date.getDayOfMonth()))
                            .count()
                            .flatMap(e -> {
                                if (e >= c.getCantidadMovPermitido()) {
                                    return Mono.just(true);
                                } else {
                                    return Mono.just(false);
                                }
                            })
                            .flatMap(f -> {
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
                                                    .flatMap(g -> {
                                                        //Aplicando comisión
                                                        if (f) {
                                                            SavingAccountMovEntity comisionEntity = new SavingAccountMovEntity();
                                                            comisionEntity.setIdSavingAccount(c.getId());
                                                            comisionEntity.setFechaMovimiento(LocalDate.now());
                                                            comisionEntity.setTipoMovimiento("COMISION");
                                                            comisionEntity.setMontoMovimiento(c.getComision() * d.getMontoMovimiento());
                                                            return savingAccountMovRepository.save(comisionEntity)
                                                                    .flatMap(h -> {
                                                                        c.setSaldoDisponible(c.getSaldoDisponible() - c.getComision() * d.getMontoMovimiento());
                                                                        return savingAccountRepository.save(c)
                                                                                .flatMap(i -> {
                                                                                    List<SavingAccountMovDto> listMovDto = new ArrayList<SavingAccountMovDto>();
                                                                                    listMovDto.add(savingAccountMovMapper.toSavingAccountMovDto(h));
                                                                                    listMovDto.add(savingAccountMovMapper.toSavingAccountMovDto(d));
                                                                                    return Mono.just(listMovDto);
                                                                                });
                                                                    });
                                                        }
                                                        List<SavingAccountMovDto> listMovDto = new ArrayList<SavingAccountMovDto>();
                                                        listMovDto.add(savingAccountMovMapper.toSavingAccountMovDto(d));
                                                        return Mono.just(listMovDto);
                                                    });
                                        });
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

    @Override
    @Transactional
    public Mono<SavingAccountMovDto> realizarTransferencia(SavingAccountTransferenciaDto savingAccountTransferenciaDto) {
        //Obtener datos de la cuenta origen
        return savingAccountRepository.findByNumeroCuenta(savingAccountTransferenciaDto.getCuentaOrigen())
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta origen no existe!!!"));
                }))
                .flatMap(a -> {
                    //Validar si existe el saldo disponible
                    if (a.getSaldoDisponible() < savingAccountTransferenciaDto.getMontoTransferencia()) {
                        return Mono.error(() -> new CustomBusinessException("MONTO_NO_DISPONIBLE", "No cuenta con saldo disponible para la transferencia!!!"));
                    }
                    SavingAccountMovEntity savingAccountMovOrigen = new SavingAccountMovEntity();
                    savingAccountMovOrigen.setIdSavingAccount(a.getId());
                    savingAccountMovOrigen.setMontoMovimiento(savingAccountTransferenciaDto.getMontoTransferencia());
                    savingAccountMovOrigen.setFechaMovimiento(LocalDate.now());
                    savingAccountMovOrigen.setTipoMovimiento("RETIRO");
                    //Registrar retiro de la cuenta orgien
                    return savingAccountMovRepository.save(savingAccountMovOrigen)
                            .flatMap(b -> {
                                a.setSaldoDisponible(a.getSaldoDisponible() - savingAccountTransferenciaDto.getMontoTransferencia());
                                //Actualizar saldo disponible en la cuenta origen
                                return savingAccountRepository.save(a)
                                        .flatMap(c -> {
                                            //Buscar datos de la cuenta destino
                                            return savingAccountRepository.findByNumeroCuenta(savingAccountTransferenciaDto.getCuentaDestino())
                                                    .switchIfEmpty(Mono.defer(() -> {
                                                        return Mono.error(() -> new CustomBusinessException("ACCOUNT_NOT_EXIST", "Cuenta destino no existe!!!"));
                                                    }))
                                                    .flatMap(d -> {
                                                        SavingAccountMovEntity savingAccountMovDestino = new SavingAccountMovEntity();
                                                        savingAccountMovDestino.setIdSavingAccount(d.getId());
                                                        savingAccountMovDestino.setMontoMovimiento(savingAccountTransferenciaDto.getMontoTransferencia());
                                                        savingAccountMovDestino.setFechaMovimiento(LocalDate.now());
                                                        savingAccountMovDestino.setTipoMovimiento("DEPOSITO");
                                                        //Registrar depósito en la cuenta destino
                                                        return savingAccountMovRepository.save(savingAccountMovDestino)
                                                                .flatMap(e -> {
                                                                    d.setSaldoDisponible(d.getSaldoDisponible() + savingAccountTransferenciaDto.getMontoTransferencia());
                                                                    //Actualizar el saldo disponible en la cuenta destino
                                                                    return savingAccountRepository.save(d)
                                                                            .flatMap(f -> {
                                                                                //Crear un Mono<SavingAccountMovDto> con el resultado del movimiento de la cuenta origen
                                                                                return Mono.just(savingAccountMovMapper.toSavingAccountMovDto(b));
                                                                            });
                                                                });
                                                    });
                                        });
                            });
                });
    }
}
