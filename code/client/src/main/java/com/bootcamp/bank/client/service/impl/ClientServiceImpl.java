package com.bootcamp.bank.client.service.impl;

import com.bootcamp.bank.client.dto.ClientDto;
import com.bootcamp.bank.client.entity.ClientEntity;
import com.bootcamp.bank.client.exception.CustomBusinessException;
import com.bootcamp.bank.client.exception.ErrorModelException;
import com.bootcamp.bank.client.mapper.ClientMapper;
import com.bootcamp.bank.client.repository.ClientRepository;
import com.bootcamp.bank.client.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.shaded.freemarker.core.ReturnInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientMapper clientMapper;

    @Override
    public Mono<ClientDto> save(ClientDto clientDto) {
        log.info("iniciando save");
        ClientEntity clientEntity = clientMapper.clientDtoToClientEntity(clientDto);
        Mono<ClientEntity> clientEntityMono = clientRepository.findByTipoDocumentoAndNumeroDocumento(clientEntity.getTipoDocumento(), clientEntity.getNumeroDocumento());

        return clientEntityMono
                .flatMap(c -> {
                    log.info("existe");
                    List<ErrorModelException> listError= new ArrayList<>();
                    return Mono.error(() -> new CustomBusinessException("CLIENT_EXIST","Cliente ya existe!!!"));
                }).switchIfEmpty(Mono.defer(() -> {
                    log.info("Cliente no existe");
                    return clientRepository.save(clientEntity);
                })).flatMap(c -> Mono.just(clientMapper.clientEntityToClientDto((ClientEntity) c)));
    }

    @Override
    public Mono<ClientDto> update(String tipoDocumento, String numeroDocumento, ClientDto clientDto) {

        Mono<ClientEntity> clientEntityMono = clientRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);

        return clientEntityMono
                .flatMap(c->{
                    c.setNombre(clientDto.getNombre());
                    c.setApellidoPaterno(clientDto.getApellidoPaterno());
                    c.setApellidoMaterno(clientDto.getApellidoMaterno());
                    c.setGenero(clientDto.getGenero());
                    c.setTipoCliente(clientDto.getTipoCliente());
                    c.setFechaNacimiento(clientDto.getFechaNacimiento());
                    return clientRepository.save(c);
                })
                .switchIfEmpty(Mono.defer(()->{
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST","Cliente no existe!!!"));
                })).flatMap(c-> Mono.just (clientMapper.clientEntityToClientDto((ClientEntity)c)));
    }

    @Override
    public Mono<ClientDto> findClient(String tipoDocumento, String numeroDocumento) {
        Mono<ClientEntity> clientEntityMono = clientRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
        return clientEntityMono
                .switchIfEmpty(Mono.defer(()->{
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST","Cliente no existe!!!"));
                }))
                .flatMap(c-> Mono.just(clientMapper.clientEntityToClientDto(c)));
    }

    @Override
    public Mono<Void> deleteClient(String tipoDocumento, String numeroDocumento) {
        Mono<ClientEntity> clientEntityMono = clientRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
        return clientEntityMono
                .switchIfEmpty(Mono.defer(()->{
                    return Mono.error(() -> new CustomBusinessException("CLIENT_NOT_EXIST","Cliente no existe!!!"));
                }))
                .flatMap(c-> clientRepository.deleteById(c.getId()));
    }

    @Override
    public Mono<Boolean> existsClient(String tipoDocumento, String numeroDocumento) {
        return clientRepository.existsByTipoDocumentoAndNumeroDocumento(tipoDocumento,numeroDocumento);
    }
}
