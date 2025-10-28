package com.pinnguino.academy.segunda_evaluacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CantidadVotosDTO {

    private String tipoEntidad; // Candidato o Partido Político
    private String nombre;
    private int cantidadVotos;

}
