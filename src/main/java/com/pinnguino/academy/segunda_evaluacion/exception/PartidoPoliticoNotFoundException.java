package com.pinnguino.academy.segunda_evaluacion.exception;

public class PartidoPoliticoNotFoundException extends Exception {
    public PartidoPoliticoNotFoundException(Long id) {
        super("Partido con ID " + id + " no encontrado.");
    }
}
