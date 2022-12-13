package com.bootcamp.bank.credit.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document("credit_mov")
public class CreditMovEntity {
    @Id
    private String id;

    @Field(value = "id_credit")
    private String idCredit;

    @Field(value = "tipo_movimiento")
    private String tipoMovimiento;

    @Field(value = "monto_movimiento")
    private double montoMovimiento;

    @Field(value = "fecha_movimiento")
    private LocalDate fechaMovimiento;
}
