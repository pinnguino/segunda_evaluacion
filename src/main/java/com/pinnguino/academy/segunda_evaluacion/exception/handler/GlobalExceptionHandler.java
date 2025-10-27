package com.pinnguino.academy.segunda_evaluacion.exception.handler;

import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PartidoPoliticoNoEncontradoException.class)
    public ResponseEntity<String> handlePartidoNoEncontradoException(PartidoPoliticoNoEncontradoException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
