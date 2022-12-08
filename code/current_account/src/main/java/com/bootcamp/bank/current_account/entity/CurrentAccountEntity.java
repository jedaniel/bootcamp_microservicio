package com.bootcamp.bank.current_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Data
@Document(value = "current_account")
public class CurrentAccountEntity {
    @Id
    private String id;

    @Field(value = "tipo_cuenta")
    private String tipoCuenta;

    @Field(value = "tipo_documento")
    private String tipoDocumento;

    @Field(value = "numero_documento")
    private String numeroDocumento;

    @Field(value = "numero_cuenta")
    private String numeroCuenta;

    @Field(value = "moneda_cuenta")
    private String monedaCuenta;

    @Field(value = "fecha_apertura")
    private LocalDate fechaApertura;

    @Field(value = "saldo_disponible")
    private double saldoDisponible;

    @Field(value = "estado")
    private String estado;

    @Field(value = "comision_mantenimiento")
    private double comisionMantenimiento;

    @Field(value = "titulares")
    private Collection<CurrentAccountTitularEntity> titulares = new HashSet<CurrentAccountTitularEntity>();

    @Field(value = "firmantes")
    private Collection<CurrentAccountFirmanteEntity> firmantes = new HashSet<CurrentAccountFirmanteEntity>();


}
