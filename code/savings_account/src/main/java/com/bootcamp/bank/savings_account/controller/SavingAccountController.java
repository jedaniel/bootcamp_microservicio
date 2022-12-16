package com.bootcamp.bank.savings_account.controller;

import com.bootcamp.bank.savings_account.dto.SavingAccountDto;
import com.bootcamp.bank.savings_account.dto.SavingAccountMovDto;
import com.bootcamp.bank.savings_account.service.SavingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("v1/saving_account")
public class SavingAccountController {
    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    SavingAccountService savingAccountService;

    @PostMapping
    public Mono<ResponseEntity<SavingAccountDto>> create(@Valid @RequestBody SavingAccountDto savingAccountDto) {
        return savingAccountService.crearCuenta(savingAccountDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "saving_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/transaction/{numeroCuenta}")
    public Mono<ResponseEntity<List<SavingAccountMovDto>>> transaction(@PathVariable String numeroCuenta, @Valid @RequestBody SavingAccountMovDto savingAccountMovDto) {
        return savingAccountService.realizarTransaccion(numeroCuenta, savingAccountMovDto)
                        .flatMap(c->Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c)))
                                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/detail/{numeroCuenta}")
    public Mono<ResponseEntity<SavingAccountDto>> detalleCuenta(@PathVariable String numeroCuenta) {
        return savingAccountService.consultarCuenta(numeroCuenta)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "saving_account", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/list/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Flux<SavingAccountDto>>> listarCuentas(@PathVariable String tipoDocumento, @PathVariable String numeroDocumento) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(savingAccountService.listarCuenta(tipoDocumento, numeroDocumento)));
    }

    @GetMapping("/movements/{numeroCuenta}")
    public Mono<ResponseEntity<Flux<SavingAccountMovDto>>> consultarMovimiento(@PathVariable String numeroCuenta) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(savingAccountService.consultarMovimiento(numeroCuenta)));
    }

}
