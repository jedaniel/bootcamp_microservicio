package com.bootcamp.bank.fixed_account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(value = "fixed_account_mov")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedAccountMovEntity {
    @Id
    private String id;

    @Field("ftipo_movimiento")
    private String tipoMovimiento;

    @Field("monto_movimiento")
    private double montoMovimiento;

    @Field("fecha_movimiento")
    private LocalDate fechaMovimiento;

    @Field("id_fixed_account")
    private String idFixedAccount;

    @DocumentReference(lazy = true, lookup = "{ 'fixedAccountMovEntity' : ?#{#self._id} }")
    @ReadOnlyProperty
    private FixedAccountEntity fixedAccountEntity;
}
