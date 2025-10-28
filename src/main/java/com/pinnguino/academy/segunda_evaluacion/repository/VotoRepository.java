package com.pinnguino.academy.segunda_evaluacion.repository;

import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto,Long> {

    List<Voto> findByCandidato_Id(Long id);
    List<Voto> findByCandidato_PartidoPolitico_Id(Long id);

}
