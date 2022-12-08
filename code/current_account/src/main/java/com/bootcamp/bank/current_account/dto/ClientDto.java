package com.bootcamp.bank.current_account.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientDto {

    private String id;

    private String tipoCliente;

    private String tipoDocumento;

    private String numeroDocumento;

    private String razonSocial;

    private String nombre;

    private String apellidoPaterno;

    private String apellidoMaterno;

    private LocalDate fechaNacimiento;

    private String genero;

    private String codeError;

    private String mensajeError;

}
