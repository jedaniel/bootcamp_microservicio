package com.bootcamp.bank.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<ErrorModelException>> handlerFieldValidate(WebExchangeBindException methodArgumentNotValidException) {
        List<ErrorModelException> errorModelExceptions = new ArrayList<>();
        List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            ErrorModelException errorModelException = new ErrorModelException();
            errorModelException.setCodeError(fieldError.getField());
            errorModelException.setMensajeError(fieldError.getDefaultMessage());
            errorModelExceptions.add(errorModelException);
        }
        return new ResponseEntity<List<ErrorModelException>>(errorModelExceptions, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<ErrorModelException> handlerBusinessException(CustomBusinessException customException) {
        return new ResponseEntity<ErrorModelException>(customException.getError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomBusinessExceptions.class)
    public ResponseEntity<List<ErrorModelException>> handlerBusinessException(CustomBusinessExceptions customExceptions) {
        return new ResponseEntity<List<ErrorModelException>>(customExceptions.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<List<ErrorModelException>> handlerNoContentException(CustomNotFoundException customNotFoundException) {
        return new ResponseEntity<List<ErrorModelException>>(customNotFoundException.getErrors(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModelException> handlerGlobalpException(Exception exception) {
        ErrorModelException errorModelException = new ErrorModelException();
        errorModelException.setCodeError("SERVER_ERROR");
        errorModelException.setMensajeError(exception.getMessage());
        return new ResponseEntity<ErrorModelException>(errorModelException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
