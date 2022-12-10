package com.bootcamp.banc.credit_card.mapper;

import com.bootcamp.banc.credit_card.dto.CreditCardDto;
import com.bootcamp.banc.credit_card.entity.CreditCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
    CreditCardMapper MAPPER= Mappers.getMapper(CreditCardMapper.class);
    CreditCardDto toCreditCardDto(CreditCardEntity creditCardEntity);

    CreditCardEntity toCreditCardEntity(CreditCardDto creditCardDto);
}
