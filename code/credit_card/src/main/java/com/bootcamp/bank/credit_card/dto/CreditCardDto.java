package com.bootcamp.bank.credit_card.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardDto {

    @JsonIgnore
    private String id;

    private String tipoTarjeta;

    private String tipoDocumento;

    private String numeroDocumento;

    private String numeroTarjeta;

    private String monedaTarjeta;

    private double limiteCredito;

    private double saldoDisponible;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaContrato;

    private String estado;
}
