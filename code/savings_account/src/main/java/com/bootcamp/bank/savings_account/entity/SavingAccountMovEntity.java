package com.bootcamp.bank.savings_account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document("saving_account_mov")
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccountMovEntity {
    @Id
    private String id;

    @Field("ftipo_movimiento")
    private String tipoMovimiento;

    @Field("monto_movimiento")
    private double montoMovimiento;

    @Field("fecha_movimiento")
    private LocalDate fechaMovimiento;

    @Field("id_saving_account")
    private String idSavingAccount;
}
