package com.bootcamp.bank.fixed_account.controller;

import com.bootcamp.bank.fixed_account.dto.FixedAccountDto;
import com.bootcamp.bank.fixed_account.dto.FixedAccountMovDto;
import com.bootcamp.bank.fixed_account.service.FixedAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("v1/fixed_account")
public class FixedAccountController {
    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    FixedAccountService fixedAccountService;

    @PostMapping
    public Mono<ResponseEntity<FixedAccountDto>> create(@Valid @RequestBody FixedAccountDto fixedAccountDto){
        return fixedAccountService.create(fixedAccountDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "fixed_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/transaction/{numeroCuenta}")
    public Mono<ResponseEntity<FixedAccountMovDto>> transaction(@PathVariable String numeroCuenta, @Valid @RequestBody FixedAccountMovDto fixedAccountMovDto){
        return fixedAccountService.realizarMovimiento(numeroCuenta, fixedAccountMovDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "fixed_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/detail/{numeroCuenta}")
    public Mono<ResponseEntity<FixedAccountDto>> detalleCuenta(@PathVariable String numeroCuenta){
        return fixedAccountService.consultarCuenta(numeroCuenta)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "fixed_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/list/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Flux<FixedAccountDto>>> listarCuentas(@PathVariable String tipoDocumento, @PathVariable String numeroDocumento){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fixedAccountService.listarCuenta(tipoDocumento, numeroDocumento)));
    }

    @GetMapping("/movements/{numeroCuenta}")
    public Mono<ResponseEntity<Flux<FixedAccountMovDto>>> consultarMovimiento(@PathVariable String numeroCuenta){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fixedAccountService.consultarMovimiento(numeroCuenta)));
    }
}
