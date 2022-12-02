package com.bootcamp.bank.client.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private ErrorModelException error;

    public CustomBusinessException(String codeError, String mensajeError) {
        error = new ErrorModelException();
        error.setCodeError(codeError);
        error.setMensajeError(mensajeError);
    }
}

