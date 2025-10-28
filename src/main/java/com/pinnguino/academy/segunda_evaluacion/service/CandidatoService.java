package com.pinnguino.academy.segunda_evaluacion.service;

import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNoEncontradoException;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    private final CandidatoRepository repository;
    private final PartidoPoliticoService partidoPoliticoService;

    @Autowired
    public CandidatoService(CandidatoRepository candidatoRepository,  PartidoPoliticoService partidoPoliticoService) {
        this.repository = candidatoRepository;
        this.partidoPoliticoService = partidoPoliticoService;
    }

    public List<Candidato> init(List<Candidato> candidatos) {
        return repository.saveAll(candidatos);
    }

    public List<Candidato> getAll() {

        return repository.findAll();

    }

    public Candidato getById(Long id)
            throws CandidatoNotFoundException {

        return repository.findById(id).orElseThrow(() -> new CandidatoNotFoundException(id));

    }

    public Candidato create(Candidato candidato)
            throws DatosInvalidosException, PartidoPoliticoNoEncontradoException {

        Long partidoPoliticoId = candidato.getPartidoPolitico().getId();
        if(partidoPoliticoId == null){
            throw new DatosInvalidosException();
        }
        else if(!partidoPoliticoService.exists(partidoPoliticoId)) {
            throw new PartidoPoliticoNoEncontradoException(partidoPoliticoId);
        }

        candidato.setPartidoPolitico(partidoPoliticoService.getById(partidoPoliticoId));
        return repository.save(candidato);
    }

    public Candidato update(Long id, Candidato actualizado)
            throws DatosInvalidosException, PartidoPoliticoNoEncontradoException, CandidatoNotFoundException {

        Candidato actual =  repository.findById(id).orElseThrow(() -> new CandidatoNotFoundException(id));
        Long idPartidoPolitico = actualizado.getPartidoPolitico().getId();

        if(actualizado.getPartidoPolitico().getId() == null) {
            throw new DatosInvalidosException();
        }
        else if(!partidoPoliticoService.exists(idPartidoPolitico)) {
            throw new PartidoPoliticoNoEncontradoException(idPartidoPolitico);
        }

        actual.setNombreCompleto(actualizado.getNombreCompleto());
        actual.setPartidoPolitico(partidoPoliticoService.getById(idPartidoPolitico));

        return repository.save(actual);

    }

    public void delete(Long id)
            throws CandidatoNotFoundException {

        if(!repository.existsById(id)) {
            throw new CandidatoNotFoundException(id);
        }

        repository.deleteById(id);

    }

}
