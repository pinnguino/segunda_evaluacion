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
@Table(name = "candidato")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCompleto;

    @ManyToOne
    @JoinColumn(name = "partido_politico_id", nullable = false)
    private PartidoPolitico partidoPolitico;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Candidato candidato = (Candidato) obj;
        return Objects.equals(this.id, candidato.id)
                && Objects.equals(this.nombreCompleto, candidato.nombreCompleto)
                && Objects.equals(this.partidoPolitico, candidato.partidoPolitico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreCompleto, partidoPolitico);
    }
}
