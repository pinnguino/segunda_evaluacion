package com.pinnguino.academy.segunda_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "partido_politico")
public class PartidoPolitico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String sigla;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        PartidoPolitico partido = (PartidoPolitico) obj;
        return Objects.equals(id, partido.id) && Objects.equals(nombre, partido.nombre) && Objects.equals(sigla, partido.sigla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, sigla);
    }
}
