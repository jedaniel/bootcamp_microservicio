package com.bootcamp.bank.fixed_account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class FixedAccountDto {

    @JsonIgnore
    private String id;

    private String tipoDocumento;

    private String numeroDocumento;

    private String numeroCuenta;

    private String monedaCuenta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaInicioVigencia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaFinVigencia;

    private int plazoDia;

    private int cantidadMovimiento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaUltimoMovimiento;

    private int diaPermiteMovimiento;

    private double montoDeposito;

    private double saldoDisponible;

    private FixedAccountMovDto movimiento;
}
