package com.bootcamp.banc.credit_card.service.impl;

import com.bootcamp.banc.credit_card.dto.ClientDto;
import com.bootcamp.banc.credit_card.dto.CreditCardDto;
import com.bootcamp.banc.credit_card.dto.CreditCardMovDto;
import com.bootcamp.banc.credit_card.exception.CustomBusinessException;
import com.bootcamp.banc.credit_card.mapper.CreditCardMapper;
import com.bootcamp.banc.credit_card.mapper.CreditCardMovMapper;
import com.bootcamp.banc.credit_card.repository.CreditCardMovRepository;
import com.bootcamp.banc.credit_card.repository.CreditCardRepository;
import com.bootcamp.banc.credit_card.service.CreditCardService;
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
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    CreditCardMovRepository creditCardMovRepository;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Autowired
    CreditCardMovMapper creditCardMovMapper;

    @Value("${uri.client}")
    String uri_client;

    @Override
    public Mono<CreditCardDto> contratarTarjeta(CreditCardDto creditCardDto) {
        return WebClient.create()
                .get()
                .uri(uri_client + "/v1/client/" + creditCardDto.getTipoDocumento() + "/" + creditCardDto.getNumeroDocumento())
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
                    creditCardDto.setNumeroTarjeta(java.util.UUID.randomUUID().toString());
                    creditCardDto.setEstado("ACTIVO");
                    creditCardDto.setTipoTarjeta(b.getTipoCliente());
                    creditCardDto.setSaldoDisponible(creditCardDto.getLimiteCredito());
                    return creditCardRepository.save(creditCardMapper.toCreditCardEntity(creditCardDto))
                            .flatMap(c -> Mono.just(creditCardMapper.toCreditCardDto(c)));
                });

    }

    @Override
    public Mono<CreditCardDto> consultarTarjeta(String numeroTarjeta) {

        return creditCardRepository.findByNumeroTarjetaAndEstado(numeroTarjeta, "ACTIVO")
                .flatMap(c -> Mono.just(creditCardMapper.toCreditCardDto(c)))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("NUMBER_CARD_NOT_EXIST", "Número de tarjeta no existe!!!"));
                }));
    }

    @Override
    public Mono<CreditCardMovDto> operarTarjeta(String numeroTarjeta, CreditCardMovDto creditCardMovDto) {

        return creditCardRepository.findByNumeroTarjetaAndEstado(numeroTarjeta, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("NUMBER_CARD_NOT_EXIST", "Número de tarjeta no existe!!!"));
                }))
                .flatMap(c -> {
                    creditCardMovDto.setIdCreditCard(c.getId());
                    return creditCardMovRepository.save(creditCardMovMapper.toCreditCardMovEntity(creditCardMovDto))
                            .flatMap(d -> {
                                if (creditCardMovDto.getTipoMovimiento().equals("CONSUMO")) {
                                    if (c.getSaldoDisponible() - d.getMontoMovimiento() < 0) {
                                        return Mono.error(() -> new CustomBusinessException("CREDITO_NO_DISPONIBLE", "No tiene crédito disponible!!!"));
                                    } else {
                                        c.setSaldoDisponible(c.getSaldoDisponible() - d.getMontoMovimiento());
                                    }
                                } else if (creditCardMovDto.getTipoMovimiento().equals("ABONO")) {
                                    c.setSaldoDisponible(c.getSaldoDisponible() + d.getMontoMovimiento());
                                }
                                return creditCardRepository.save(c)
                                        .flatMap(e -> Mono.just(creditCardMovMapper.toCreditCardMovDto(d)));
                            });
                });
    }

    @Override
    public Flux<CreditCardDto> listarTarjetas(String tipoDocumento, String numeroDocumento) {
        return creditCardRepository.findByTipoDocumentoAndNumeroDocumentoAndEstado(tipoDocumento, numeroDocumento, "ACTIVO")
                .flatMap(c -> Flux.just(creditCardMapper.toCreditCardDto(c)))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("NOT_FOUND_CARD", "No se encontró tarjetas para el cliente!!!"));
                }));
    }

    @Override
    public Flux<CreditCardMovDto> listarMovimientos(String numeroTarjeta) {
        return creditCardRepository.findByNumeroTarjetaAndEstado(numeroTarjeta, "ACTIVO")
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.error(() -> new CustomBusinessException("NUMBER_CARD_NOT_EXIST", "Número de tarjeta no existe!!!"));
                }))
                .flux()
                .flatMap(c -> {
                    return creditCardMovRepository.findByIdCreditCard(c.getId())
                            .flatMap(d -> Flux.just(creditCardMovMapper.toCreditCardMovDto(d)));
                });
    }
}
