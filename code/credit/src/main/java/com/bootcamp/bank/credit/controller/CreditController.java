package com.bootcamp.bank.credit.controller;

import com.bootcamp.bank.credit.dto.CreditDto;
import com.bootcamp.bank.credit.dto.CreditMovDto;
import com.bootcamp.bank.credit.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("v1/credit")
public class CreditController {
    @Autowired
    CreditService creditService;

    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @PostMapping
    public Mono<ResponseEntity<CreditDto>> obtenerCredito(@RequestBody CreditDto creditDto) {

        return creditService.obtenerCredito(creditDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "credit_card", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{numeroCredito}")
    public Mono<ResponseEntity<CreditDto>> findCredito(@PathVariable String numeroCredito) {
        return creditService.buscarCredito(numeroCredito)
                .flatMap(c -> Mono.just(ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar_credito/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Flux<CreditDto>>> listarCreditos(@PathVariable String tipoDocumento, @PathVariable String numeroDocumento) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(creditService.listarCreditos(tipoDocumento, numeroDocumento)));
    }

    @PostMapping("/movimiento/{numeroCredito}")
    public Mono<ResponseEntity<CreditMovDto>> realizarTransaccionCredito(@PathVariable String numeroCredito, @RequestBody CreditMovDto creditMovDto) {
        return creditService.realizarTransaccionCredito(numeroCredito, creditMovDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar_movimiento/{numeroCredito}")
    public Mono<ResponseEntity<Flux<CreditMovDto>>> listarCreditos(@PathVariable String numeroCredito) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(creditService.listarMovimientoCredito(numeroCredito)));
    }
}
