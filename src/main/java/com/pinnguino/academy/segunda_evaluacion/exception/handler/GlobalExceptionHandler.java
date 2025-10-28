package com.pinnguino.academy.segunda_evaluacion.exception.handler;

import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PartidoPoliticoNotFoundException.class)
    public ResponseEntity<String> handlePartidoNoEncontradoException(PartidoPoliticoNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CandidatoNotFoundException.class)
    public ResponseEntity<String> handleCandidatoNotFoundException(CandidatoNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<String> handleDatosInvalidosException(DatosInvalidosException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
