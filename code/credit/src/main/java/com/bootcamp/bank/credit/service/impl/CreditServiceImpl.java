package com.bootcamp.bank.credit.service.impl;

import com.bootcamp.bank.credit.dto.ClientDto;
import com.bootcamp.bank.credit.dto.CreditDto;
import com.bootcamp.bank.credit.dto.CreditMovDto;
import com.bootcamp.bank.credit.exception.CustomBusinessException;
import com.bootcamp.bank.credit.mapper.CreditMapper;
import com.bootcamp.bank.credit.mapper.CreditMovMapper;
import com.bootcamp.bank.credit.repository.CreditMovRepository;
import com.bootcamp.bank.credit.repository.CreditRepository;
import com.bootcamp.bank.credit.service.CreditService;
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
public class CreditServiceImpl implements CreditService {

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    CreditMovRepository creditMovRepository;

    @Autowired
    CreditMapper creditMapper;

    @Autowired
    CreditMovMapper creditMovMapper;

    @Value("${uri.client}")
    String uri_client;

    @Override
    public Mono<CreditDto> obtenerCredito(CreditDto creditDto) {

        return WebClient.create()
                .get()
                .uri(uri_client + "/v1/client/" + creditDto.getTipoDocumento() + "/" + creditDto.getNumeroDocumento())
                .httpRequest(httpRequest -> {
                    HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
                    reactorRequest.responseTimeout(Duration.ofSeconds(2));
                })
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return Mono.error(() -> new CustomBusinessException("ERROR", "Ocurrió un error interno!!!"));
                })
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Cliente no existe para aperturar cuenta!!!"));
                })
                .bodyToMono(ClientDto.class)
                .flatMap(b -> {
                    if (b.getTipoCliente().equals("PERSONAL")) {
                        return creditRepository.findByTipoDocumentoAndNumeroDocumentoAndEstado(creditDto.getTipoDocumento(), creditDto.getNumeroDocumento(), "ACTIVO")
                                .count()
                                .flatMap(c -> {
                                    if (c > 0) {
                                        return Mono.error(() -> new CustomBusinessException("CREDITO_NO_PERMITIDO", "El cliente ya tiene un crédito!!!"));
                                    } else {
                                        creditDto.setNumeroCredito(java.util.UUID.randomUUID().toString());
                                        creditDto.setDeudaPendiente(creditDto.getMontoCredito());
                                        creditDto.setEstado("ACTIVO");
                                        return creditRepository.save(creditMapper.toCreditEntity(creditDto))
                                                .flatMap(d -> {
                                                    CreditMovDto creditMovDto = new CreditMovDto();
                                                    creditMovDto.setIdCredit(d.getId());
                                                    creditMovDto.setFechaMovimiento(d.getFechaCredito());
                                                    creditMovDto.setTipoMovimiento("RETIRO");
                                                    creditMovDto.setMontoMovimiento(d.getMontoCredito());
                                                    return creditMovRepository.save(creditMovMapper.toCreditMovEntity(creditMovDto))
                                                            .flatMap(e -> Mono.just(creditMapper.toCreditDto(d)));
                                                });
                                    }
                                });

                    } else if (b.getTipoCliente().equals("EMPRESARIAL")) {
                        creditDto.setNumeroCredito(java.util.UUID.randomUUID().toString());
                        creditDto.setDeudaPendiente(creditDto.getMontoCredito());
                        creditDto.setEstado("ACTIVO");
                        return creditRepository.save(creditMapper.toCreditEntity(creditDto))
                                .flatMap(d -> {
                                    CreditMovDto creditMovDto = new CreditMovDto();
                                    creditMovDto.setIdCredit(d.getId());
                                    creditMovDto.setFechaMovimiento(d.getFechaCredito());
                                    creditMovDto.setTipoMovimiento("RETIRO");
                                    creditMovDto.setMontoMovimiento(d.getMontoCredito());
                                    return creditMovRepository.save(creditMovMapper.toCreditMovEntity(creditMovDto))
                                            .flatMap(e -> Mono.just(creditMapper.toCreditDto(d)));
                                });
                    } else {
                        return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST", "Tipo de cliente no existe!!!"));
                    }
                });
    }

    @Override
    public Mono<CreditDto> buscarCredito(String numeroCredito) {
        return creditRepository.findByNumeroCreditoAndEstado(numeroCredito, "ACTIVO")
                .flatMap(c -> Mono.just(creditMapper.toCreditDto(c)))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("CREDIT_NOT_EXIST", "Número de crédito no existe!!!"));
                }));
    }

    @Override
    public Flux<CreditDto> listarCreditos(String tipoDocumento, String numeroDocumento) {
        return creditRepository.findByTipoDocumentoAndNumeroDocumentoAndEstado(tipoDocumento, numeroDocumento, "ACTIVO")
                .flatMap(c -> Flux.just(creditMapper.toCreditDto(c)))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("CREDIT_NOT_EXIST", "Cliente no tiene crédito!!!"));
                }));
    }

    @Override
    public Mono<CreditMovDto> realizarTransaccionCredito(String numeroCredito, CreditMovDto creditMovDto) {
        return creditRepository.findByNumeroCreditoAndEstado(numeroCredito, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("CREDIT_NOT_EXIST", "Número de crédito no existe!!!"));
                }))
                .flatMap(c -> {
                    creditMovDto.setIdCredit(c.getId());
                    return creditMovRepository.save(creditMovMapper.toCreditMovEntity(creditMovDto))
                            .flatMap(d -> {
                                c.setDeudaPendiente(c.getDeudaPendiente() - d.getMontoMovimiento());
                                return creditRepository.save(c)
                                        .flatMap(e -> Mono.just(creditMovMapper.toCreditMovDto(d)));
                            });
                });
    }

    @Override
    public Flux<CreditMovDto> listarMovimientoCredito(String numeroCredito) {
        return creditRepository.findByNumeroCreditoAndEstado(numeroCredito, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("CREDIT_NOT_EXIST", "Número de crédito no existe!!!"));
                }))
                .flux()
                .flatMap(c ->
                        creditMovRepository.findByIdCredit(c.getId())
                                .flatMap(d -> Flux.just(creditMovMapper.toCreditMovDto(d)))
                );
    }
}
