package com.bootcamp.bank.current_account.mapper;

import com.bootcamp.bank.current_account.dto.CurrentAccountMovDto;
import com.bootcamp.bank.current_account.dto.CurrentAccountTitularDto;
import com.bootcamp.bank.current_account.entity.CurrentAccountMovEntity;
import com.bootcamp.bank.current_account.entity.CurrentAccountTitularEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CurrentAccountMovMapper {
    CurrentAccountMovMapper MAPPER= Mappers.getMapper(CurrentAccountMovMapper.class);
    CurrentAccountMovDto toCurrentAccountMovDto(CurrentAccountMovEntity currentAccountMovEntity);

    CurrentAccountMovEntity toCurrentAccountMovEntity(CurrentAccountMovDto currentAccountMovDto);
}
