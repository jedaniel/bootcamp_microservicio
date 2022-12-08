package com.bootcamp.bank.current_account.entity;

import lombok.Data;

@Data
public class CurrentAccountTitularEntity {
    //private CurrentAccountEntity currentAccount;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    private String telefono;

}
