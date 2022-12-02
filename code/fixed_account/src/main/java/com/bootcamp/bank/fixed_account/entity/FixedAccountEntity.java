package com.bootcamp.bank.fixed_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(value = "fixed_account")
@Data
public class FixedAccountEntity {
    @Id
    private String id;

    @Field("tipo_documento")
    private String tipoDocumento;

    @Field("numero_documento")
    private String numeroDocumento;

    @Field("numero_cuenta")
    private String numeroCuenta;

    @Field("moneda_cuenta")
    private String monedaCuenta;

    @Field("fecha_inicio_vigencia")
    private LocalDate fechaInicioVigencia;

    @Field("fecha_fin_vigencia")
    private LocalDate fechaFinVigencia;

    @Field("plazo_dia")
    private int plazoDia;

    @Field("cantidad_movimiento")
    private int cantidadMovimiento;

    @Field("fecha_ultimo_movimiento")
    private LocalDate fechaUltimoMovimiento;

    @Field("dia_permite_movimiento")
    private int diaPermiteMovimiento;

    @Field("monto_deposito")
    private double montoDeposito;

    @Field("saldo_disponible")
    private double saldoDisponible;

    @DocumentReference(lazy = true)
    private FixedAccountMovEntity fixedAccountMovEntity;

}
