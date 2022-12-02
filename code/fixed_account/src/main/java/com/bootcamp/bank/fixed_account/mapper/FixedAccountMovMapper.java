package com.bootcamp.bank.fixed_account.mapper;

import com.bootcamp.bank.fixed_account.dto.FixedAccountMovDto;
import com.bootcamp.bank.fixed_account.entity.FixedAccountMovEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FixedAccountMovMapper {
    FixedAccountMovMapper MAPPER= Mappers.getMapper(FixedAccountMovMapper.class);
    FixedAccountMovEntity toFixedAccountMovEntity(FixedAccountMovDto fixedAccountMovDto);

    @InheritInverseConfiguration
    FixedAccountMovDto toFixedAccountMovDto(FixedAccountMovEntity fixedAccountMovEntity);
}
