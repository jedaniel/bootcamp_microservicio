package com.bootcamp.bank.savings_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document("saving_account")
public class SavingAccountEntity {
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

    @Field("fecha_apertura")
    private LocalDate fechaApertura;

    @Field("saldo_disponible")
    private double saldoDisponible;

    @Field("cantidad_mov_actual")
    private int cantidadMovActual;

    @Field("cantidad_mov_permitido")
    private int cantidadMovPermitido;

    @Field("estado")
    private String estado;
}
