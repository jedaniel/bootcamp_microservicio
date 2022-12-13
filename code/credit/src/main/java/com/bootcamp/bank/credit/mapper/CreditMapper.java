package com.bootcamp.bank.credit.mapper;

import com.bootcamp.bank.credit.dto.CreditDto;
import com.bootcamp.bank.credit.entity.CreditEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    CreditMapper MAPPER= Mappers.getMapper(CreditMapper.class);
    CreditDto toCreditDto(CreditEntity creditEntity);

    CreditEntity toCreditEntity(CreditDto creditDto);
}
