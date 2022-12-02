package com.bootcamp.bank.fixed_account.mapper;

import com.bootcamp.bank.fixed_account.dto.FixedAccountDto;
import com.bootcamp.bank.fixed_account.entity.FixedAccountEntity;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {FixedAccountMovMapper.class})
public interface FixedAccountMapper {
    FixedAccountMapper MAPPER = Mappers.getMapper(FixedAccountMapper.class);
    @InheritInverseConfiguration
    @Mapping(source = "fixedAccountMovEntity", target = "movimiento")
    FixedAccountDto toFixedAccountDto(FixedAccountEntity fixedAccountEntity);

    FixedAccountEntity toFixedAccountEntity(FixedAccountDto fixedAccountDto);
}
