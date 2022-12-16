package com.bootcamp.bank.savings_account.dto;

import com.bootcamp.bank.savings_account.entity.SavingAccountMovEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingAccountDto {

    @JsonIgnore
    private String id;

    private String tipoDocumento;

    private String numeroDocumento;

    private String numeroCuenta;

    private String monedaCuenta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaApertura;

    private double saldoDisponible;

    private int cantidadMovActual;

    private int cantidadMovPermitido;

    private double comision;

    private String estado;

}
