package com.bootcamp.bank.client.controller;

import com.bootcamp.bank.client.dto.ClientDto;
import com.bootcamp.bank.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/client")
public class ClientController {

    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    ClientService clientService;

//    @Autowired
//    ClientMapper clientMapper;

    @PostMapping
    public Mono<ResponseEntity<ClientDto>> create(@Valid @RequestBody ClientDto clientDto){
        //log.info("create executed {}", request);
        return clientService.save(clientDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "customer", c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<ClientDto>> update( @PathVariable String tipoDocumento, @PathVariable String numeroDocumento, @RequestBody ClientDto clientDto){
        return clientService.update(tipoDocumento, numeroDocumento, clientDto)
                .flatMap(c -> Mono.just(ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<ClientDto>> findClient( @PathVariable String tipoDocumento, @PathVariable String numeroDocumento){
        return clientService.findClient(tipoDocumento, numeroDocumento)
                .flatMap(c -> Mono.just(ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Boolean>> existsClient( @PathVariable String tipoDocumento, @PathVariable String numeroDocumento){
        return clientService.existsClient(tipoDocumento, numeroDocumento)
                .flatMap(c -> Mono.just(ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{tipoDocumento}/{numeroDocumento}")
    public Mono<ResponseEntity<Void>> deleteClient( @PathVariable String tipoDocumento, @PathVariable String numeroDocumento){
        return clientService.deleteClient(tipoDocumento, numeroDocumento)
                .flatMap(c -> Mono.just(ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)));
    }
}
