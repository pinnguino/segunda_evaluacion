package com.pinnguino.academy.segunda_evaluacion.exception;

public class DatosInvalidosException extends Exception {
    public DatosInvalidosException() {
        super("El objeto contiene datos inválidos o nulos.");
    }
}
