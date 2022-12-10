package com.bootcamp.banc.credit_card.mapper;

import com.bootcamp.banc.credit_card.dto.CreditCardMovDto;
import com.bootcamp.banc.credit_card.entity.CreditCardMovEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditCardMovMapper {
    CreditCardMovMapper MAPPER= Mappers.getMapper(CreditCardMovMapper.class);

    CreditCardMovDto toCreditCardMovDto(CreditCardMovEntity creditCardMovEntity);

    CreditCardMovEntity toCreditCardMovEntity(CreditCardMovDto creditCardMovDto);
}
