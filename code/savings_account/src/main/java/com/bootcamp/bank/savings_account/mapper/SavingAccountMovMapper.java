package com.bootcamp.bank.savings_account.mapper;

import com.bootcamp.bank.savings_account.dto.SavingAccountMovDto;
import com.bootcamp.bank.savings_account.entity.SavingAccountMovEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SavingAccountMovMapper {
    SavingAccountMovMapper MAPPER = Mappers.getMapper(SavingAccountMovMapper.class);
    @InheritInverseConfiguration
    SavingAccountMovDto toSavingAccountMovDto(SavingAccountMovEntity savingAccountMovEntity);

    SavingAccountMovEntity toSavingAccountMovEntity(SavingAccountMovDto savingAccountMovDto);
}
