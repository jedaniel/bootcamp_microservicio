package com.bootcamp.bank.credit_card.mapper;

import com.bootcamp.bank.credit_card.dto.CreditCardMovDto;
import com.bootcamp.bank.credit_card.entity.CreditCardMovEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditCardMovMapper {
    CreditCardMovMapper MAPPER= Mappers.getMapper(CreditCardMovMapper.class);

    CreditCardMovDto toCreditCardMovDto(CreditCardMovEntity creditCardMovEntity);

    CreditCardMovEntity toCreditCardMovEntity(CreditCardMovDto creditCardMovDto);
}
