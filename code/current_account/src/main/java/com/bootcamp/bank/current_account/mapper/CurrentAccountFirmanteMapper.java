package com.bootcamp.bank.current_account.mapper;

import com.bootcamp.bank.current_account.dto.CurrentAccountDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountFirmanteDto;
import com.bootcamp.bank.current_account.entity.CurrentAccountEntity;
import com.bootcamp.bank.current_account.entity.CurrentAccountFirmanteEntity;
import org.mapstruct.factory.Mappers;

public interface CurrentAccountFirmanteMapper {
    CurrentAccountFirmanteMapper MAPPER= Mappers.getMapper(CurrentAccountFirmanteMapper.class);
    CurrentAccountFirmanteDto toCurrentAccountFirmanteDto(CurrentAccountFirmanteEntity currentAccountFirmanteEntity);

    CurrentAccountFirmanteEntity toCurrentAccountFirmanteEntity(CurrentAccountFirmanteDto currentAccountFirmanteDto);
}
