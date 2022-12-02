package com.bootcamp.bank.client.service;

import com.bootcamp.bank.client.dto.ClientDto;
import reactor.core.publisher.Mono;

public interface ClientService {
    public Mono<ClientDto> save(ClientDto clientDto);
    public Mono<ClientDto> update(String tipoDocumento, String numeroDocumento, ClientDto clientDto);
    public Mono<ClientDto> findClient(String tipoDocumento, String numeroDocumento);
    public Mono<Void> deleteClient(String tipoDocumento, String numeroDocumento);
    public Mono<Boolean> existsClient(String tipoDocumento, String numeroDocumento);
}
