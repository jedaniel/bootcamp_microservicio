package com.bootcamp.bank.credit.mapper;

import com.bootcamp.bank.credit.dto.CreditDto;
import com.bootcamp.bank.credit.dto.CreditMovDto;
import com.bootcamp.bank.credit.entity.CreditEntity;
import com.bootcamp.bank.credit.entity.CreditMovEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditMovMapper {
    CreditMovMapper MAPPER= Mappers.getMapper(CreditMovMapper.class);
    CreditMovDto toCreditMovDto(CreditMovEntity creditMovEntity);

    CreditMovEntity toCreditMovEntity(CreditMovDto creditMovDto);
}
