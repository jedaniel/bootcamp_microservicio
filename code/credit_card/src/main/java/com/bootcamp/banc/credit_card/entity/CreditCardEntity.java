package com.bootcamp.banc.credit_card.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(value = "credit_card")
public class CreditCardEntity {
    @Id
    private String id;

    @Field(value = "tipo_tarjeta")
    private String tipoTarjeta;

    @Field(value = "tipo_documento")
    private String tipoDocumento;

    @Field(value = "numero_documento")
    private String numeroDocumento;

    @Field(value = "numero_tarjeta")
    private String numeroTarjeta;

    @Field(value = "moneda_tarjeta")
    private String monedaTarjeta;

    @Field(value = "limite_credito")
    private double limiteCredito;

    @Field(value = "saldo_disponible")
    private double saldoDisponible;

    @Field(value = "fecha_contrato")
    private LocalDate fechaContrato;

    @Field(value = "estado")
    private String estado;

}
