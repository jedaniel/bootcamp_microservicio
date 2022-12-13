package com.bootcamp.bank.credit.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomBusinessExceptions extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<ErrorModelException> errors;

    public CustomBusinessExceptions(List<ErrorModelException> errors) {
        this.errors = errors;
    }
}
