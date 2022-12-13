package com.bootcamp.bank.credit_card.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(value = "credit_card_mov")
public class CreditCardMovEntity {
    @Id
    private String id;

    @Field(value = "id_credit_card")
    private String idCreditCard;

    @Field(value = "tipo_movimiento")
    private String tipoMovimiento;

    @Field(value = "monto_movimiento")
    private double montoMovimiento;

    @Field(value = "fecha_movimiento")
    private LocalDate fechaMovimiento;

    @Field(value = "numero_cuotas")
    private int numeroCuotas;
}
