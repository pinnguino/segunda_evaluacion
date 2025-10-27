package com.pinnguino.academy.segunda_evaluacion.repository;

import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatoRepository extends JpaRepository<PartidoPolitico, Long> {
}
