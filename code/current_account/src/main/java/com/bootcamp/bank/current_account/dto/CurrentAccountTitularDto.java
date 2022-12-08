package com.bootcamp.bank.current_account.dto;

import com.bootcamp.bank.current_account.entity.CurrentAccountEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CurrentAccountTitularDto {
    //private CurrentAccountEntity currentAccount;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    private String telefono;

}
