package com.bootcamp.bank.current_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(value = "current_account_mov")
public class CurrentAccountMovEntity {

    @Id
    private String id;

    @Field(value = "id_current_account")
    private String idCurrentAccount;

    @Field("tipo_movimiento")
    private String tipoMovimiento;

    @Field("monto_movimiento")
    private double montoMovimiento;

    @Field("fecha_movimiento")
    private LocalDate fechaMovimiento;
}
