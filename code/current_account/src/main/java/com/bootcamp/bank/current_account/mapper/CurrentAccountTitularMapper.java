package com.bootcamp.bank.current_account.mapper;

import com.bootcamp.bank.current_account.dto.CurrentAccountFirmanteDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountTitularDto;
import com.bootcamp.bank.current_account.entity.CurrentAccountFirmanteEntity;
import com.bootcamp.bank.current_account.entity.CurrentAccountTitularEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CurrentAccountTitularMapper {
    CurrentAccountTitularMapper MAPPER= Mappers.getMapper(CurrentAccountTitularMapper.class);
    CurrentAccountTitularDto toCurrentAccountTitularDto(CurrentAccountTitularEntity currentAccountTitularEntity);

    CurrentAccountTitularEntity toCurrentAccountTitularEntity(CurrentAccountTitularDto currentAccountTitularDto);
}
