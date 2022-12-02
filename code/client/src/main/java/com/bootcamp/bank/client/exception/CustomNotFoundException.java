package com.bootcamp.bank.client.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<ErrorModelException> errors;

    public CustomNotFoundException(List<ErrorModelException> errors) {
        this.errors = errors;
    }
}
