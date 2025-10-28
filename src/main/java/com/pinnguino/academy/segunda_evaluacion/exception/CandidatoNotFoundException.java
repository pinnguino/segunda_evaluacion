package com.pinnguino.academy.segunda_evaluacion.exception;

public class CandidatoNotFoundException extends Exception {
    public CandidatoNotFoundException(Long id) {
        super("Candidato con ID " + id + " no encontrado.");
    }
}
