package com.bootcamp.bank.credit_card.controller;

import com.bootcamp.bank.credit_card.dto.CreditCardDto;
import com.bootcamp.bank.credit_card.dto.CreditCardMovDto;
import com.bootcamp.bank.credit_card.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("v1/credit_card")
public class CreditCardController {

    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    CreditCardService creditCardService;

    @PostMapping
    public Mono<ResponseEntity<CreditCardDto>> createCreditCard(@RequestBody CreditCardDto creditCardDto) {
        return creditCardService.contratarTarjeta(creditCardDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "credit_card", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{numeroTarjeta}")
    public Mono<ResponseEntity<CreditCardDto>> findCreditCard(@PathVariable String numeroTarjeta) {
        return creditCardService.consultarTarjeta(numeroTarjeta)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "credit_card", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Flux<CreditCardDto>>> listCreditCard(@PathVariable String tipoDocumento, @PathVariable String numeroDocumento) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(creditCardService.listarTarjetas(tipoDocumento, numeroDocumento)));
    }

    @PostMapping("/movimiento/{numeroCuenta}")
    public Mono<ResponseEntity<CreditCardMovDto>> operarCreditCard(@PathVariable String numeroCuenta, @RequestBody CreditCardMovDto creditCardMovDto) {
        return creditCardService.operarTarjeta(numeroCuenta, creditCardMovDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "credit_card", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/movimiento/{numeroTarjeta}")
    public Mono<ResponseEntity<Flux<CreditCardMovDto>>> listCreditCard(@PathVariable String numeroTarjeta) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(creditCardService.listarMovimientos(numeroTarjeta)));
    }
}
