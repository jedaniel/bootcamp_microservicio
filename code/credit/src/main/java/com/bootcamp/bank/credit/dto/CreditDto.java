package com.bootcamp.bank.credit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditDto {
    @JsonIgnore
    private String id;
    private String tipoCredito;
    private String tipoDocumento;
    private String numeroDocumento;
    private String numeroCredito;
    private String monedaCredito;
    private double montoCredito;
    private double deudaPendiente;
    private LocalDate fechaCredito;
    private String estado;
}
