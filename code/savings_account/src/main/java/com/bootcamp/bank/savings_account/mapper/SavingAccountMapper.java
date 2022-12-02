package com.bootcamp.bank.savings_account.mapper;

import com.bootcamp.bank.savings_account.dto.SavingAccountDto;
import com.bootcamp.bank.savings_account.entity.SavingAccountEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SavingAccountMapper {
    SavingAccountMapper MAPPER = Mappers.getMapper(SavingAccountMapper.class);
    @InheritInverseConfiguration
    SavingAccountDto toSavingAccountDto(SavingAccountEntity savingAccountEntity);

    SavingAccountEntity toSavingAccountEntity(SavingAccountDto savingAccountDto);
}
