package com.bootcamp.bank.client.mapper;

import com.bootcamp.bank.client.dto.ClientDto;
import com.bootcamp.bank.client.entity.ClientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto clientEntityToClientDto(ClientEntity clientEntity);
    ClientEntity clientDtoToClientEntity(ClientDto clientDto);
}
