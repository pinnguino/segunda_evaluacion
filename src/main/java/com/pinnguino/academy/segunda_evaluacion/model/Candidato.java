package com.pinnguino.academy.segunda_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
