package com.bootcamp.bank.fixed_account.dto;

import com.bootcamp.bank.fixed_account.entity.FixedAccountEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FixedAccountMovDto {

    @JsonIgnore
    private String id;

    private String tipoMovimiento;

    private double montoMovimiento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaMovimiento;

    private FixedAccountDto fixedAccountDto;
}
