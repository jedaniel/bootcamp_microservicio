package com.bootcamp.bank.current_account.controller;

import com.bootcamp.bank.current_account.dto.CurrentAccountDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountMovDto;
import com.bootcamp.bank.current_account.service.CurrentAccountService;
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
@RequestMapping("v1/current_account")
public class CurrentAccountController {
    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    CurrentAccountService currentAccountService;

    @PostMapping
    public Mono<ResponseEntity<CurrentAccountDto>> create(@Valid @RequestBody CurrentAccountDto currentAccountDto){
        return currentAccountService.save(currentAccountDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "current_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Flux<CurrentAccountDto>>> listarCuenta(@PathVariable String tipoDocumento, @PathVariable String numeroDocumento){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(currentAccountService.listarCurrentAccount(tipoDocumento, numeroDocumento)));
    }

    @GetMapping("/{numeroCuenta}")
    public Mono<ResponseEntity<Mono<CurrentAccountDto>>> buscarCuenta(@PathVariable String numeroCuenta){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(currentAccountService.buscarCurrentAccount(numeroCuenta)));
    }

    @PostMapping("/movimiento/{numeroCuenta}")
    public Mono<ResponseEntity<CurrentAccountMovDto>> crearMovimiento(@PathVariable String numeroCuenta, @RequestBody CurrentAccountMovDto currentAccountMovDto){
        return currentAccountService.realizarMovimiento(numeroCuenta, currentAccountMovDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "current_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/movimiento/{numeroCuenta}")
    public Mono<ResponseEntity<Flux<CurrentAccountMovDto>>> listarMovimiento(@PathVariable String numeroCuenta){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(currentAccountService.listarMovimiento(numeroCuenta)));
    }
}
