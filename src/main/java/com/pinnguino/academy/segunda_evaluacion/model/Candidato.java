package com.pinnguino.academy.segunda_evaluacion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "candidato")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombreCompleto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_politico_id", nullable = false)
    private PartidoPolitico partidoPolitico;

}
