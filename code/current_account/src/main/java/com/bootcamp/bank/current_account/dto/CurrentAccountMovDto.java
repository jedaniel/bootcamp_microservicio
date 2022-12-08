package com.bootcamp.bank.current_account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CurrentAccountMovDto {

    private String id;

    private String idCurrentAccount;

    private String tipoMovimiento;

    private double montoMovimiento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaMovimiento;
}

