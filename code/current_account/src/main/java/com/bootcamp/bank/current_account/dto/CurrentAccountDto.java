package com.bootcamp.bank.current_account.dto;

import com.bootcamp.bank.current_account.entity.CurrentAccountFirmanteEntity;
import com.bootcamp.bank.current_account.entity.CurrentAccountTitularEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentAccountDto {

    private String id;

    private String tipoCuenta;

    private String tipoDocumento;

    private String numeroDocumento;

    private String numeroCuenta;

    private String monedaCuenta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaApertura;

    private double saldoDisponible;

    private String estado;

    private double comisionMantenimiento;

    private Collection<CurrentAccountTitularEntity> titulares = new HashSet<CurrentAccountTitularEntity>();

    private Collection<CurrentAccountFirmanteEntity> firmantes = new HashSet<CurrentAccountFirmanteEntity>();
}
