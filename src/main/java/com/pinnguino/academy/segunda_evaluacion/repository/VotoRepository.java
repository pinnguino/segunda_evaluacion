package com.pinnguino.academy.segunda_evaluacion.repository;

import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto,Long> {
}
