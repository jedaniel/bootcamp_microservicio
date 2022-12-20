package com.bootcamp.bank.savings_account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingAccountTransferenciaDto {
    @JsonIgnore
    private String id;

    private String cuentaOrigen;

    private String cuentaDestino;

    private String tipoTransferencia;//PROPIO, TERCERO

    private double montoTransferencia;

    private double montoComision;

}
