package com.bootcamp.bank.current_account.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorModelException {
    private String codeError;
    private String mensajeError;
}
