package com.bootcamp.bank.credit_card.mapper;

import com.bootcamp.bank.credit_card.dto.CreditCardDto;
import com.bootcamp.bank.credit_card.entity.CreditCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
    CreditCardMapper MAPPER= Mappers.getMapper(CreditCardMapper.class);
    CreditCardDto toCreditCardDto(CreditCardEntity creditCardEntity);

    CreditCardEntity toCreditCardEntity(CreditCardDto creditCardDto);
}
