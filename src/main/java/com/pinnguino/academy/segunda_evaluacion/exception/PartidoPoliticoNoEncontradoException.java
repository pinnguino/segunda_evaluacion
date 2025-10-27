package com.pinnguino.academy.segunda_evaluacion.exception;

public class PartidoPoliticoNoEncontradoException extends Exception {
    public PartidoPoliticoNoEncontradoException(Long id) {
        super("Partido con ID " + id + " no encontrado.");
    }
}
