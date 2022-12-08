package com.bootcamp.bank.current_account.mapper;

import com.bootcamp.bank.current_account.dto.CurrentAccountDto;
import com.bootcamp.bank.current_account.entity.CurrentAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CurrentAccountTitularMapper.class, CurrentAccountFirmanteMapper.class})
public interface CurrentAccountMapper {
    CurrentAccountMapper MAPPER= Mappers.getMapper(CurrentAccountMapper.class);
    CurrentAccountDto toCurrentAccountDto(CurrentAccountEntity currentAccountEntity);

    CurrentAccountEntity toCurrentAccountEntity(CurrentAccountDto currentAccountDto);
}
