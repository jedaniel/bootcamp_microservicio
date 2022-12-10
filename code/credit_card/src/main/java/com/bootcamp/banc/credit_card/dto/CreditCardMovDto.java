package com.bootcamp.banc.credit_card.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditCardMovDto {

    @JsonIgnore
    private String id;

    @JsonIgnore
    private String idCreditCard;

    private String tipoMovimiento;

    private double montoMovimiento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaMovimiento;

    private int numeroCuotas;
}
