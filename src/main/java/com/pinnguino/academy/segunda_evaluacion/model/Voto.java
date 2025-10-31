package com.pinnguino.academy.segunda_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "voto")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidato_id", nullable = true) // Entiendo que el voto puede ser nulo.
    private Candidato candidato;

    private LocalDateTime fechaEmision;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Voto voto = (Voto) obj;
        return Objects.equals(id, voto.id)
                && Objects.equals(candidato, voto.candidato)
                && Objects.equals(fechaEmision, voto.fechaEmision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, candidato, fechaEmision);
    }
}
