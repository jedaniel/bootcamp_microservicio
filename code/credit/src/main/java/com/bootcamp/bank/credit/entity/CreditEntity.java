package com.bootcamp.bank.credit.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(value = "credit")
public class CreditEntity {
    @Id
    private String id;

    @Field(value = "tipo_credito")
    private String tipoCredito;

    @Field(value = "tipo_documento")
    private String tipoDocumento;

    @Field(value = "numero_documento")
    private String numeroDocumento;

    @Field(value = "numero_credito")
    private String numeroCredito;

    @Field(value = "moneda_credito")
    private String monedaCredito;

    @Field(value = "monto_credito")
    private double montoCredito;

    @Field(value = "deuda_pendiente")
    private double deudaPendiente;

    @Field(value = "fecha_credito")
    private LocalDate fechaCredito;

    @Field(value = "estado")
    private String estado;
}
