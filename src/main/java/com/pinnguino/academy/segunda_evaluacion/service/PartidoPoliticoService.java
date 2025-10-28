package com.pinnguino.academy.segunda_evaluacion.service;

import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.repository.PartidoPoliticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidoPoliticoService {

    private final PartidoPoliticoRepository repository;

    @Autowired
    public PartidoPoliticoService(PartidoPoliticoRepository repository) {
        this.repository = repository;
    }

    public List<PartidoPolitico> init(List<PartidoPolitico> partidos) {
        return repository.saveAll(partidos);
    }

    public List<PartidoPolitico> getAll() {
        return repository.findAll();
    }

    public PartidoPolitico getById(Long id)
            throws PartidoPoliticoNotFoundException {

        return repository.findById(id).orElseThrow(() -> new PartidoPoliticoNotFoundException(id));

    }

    public PartidoPolitico create(PartidoPolitico partido) {
        return repository.save(partido);
    }

    public PartidoPolitico update(Long id, PartidoPolitico actualizado)
            throws PartidoPoliticoNotFoundException {

        PartidoPolitico actual = repository.findById(id).orElseThrow(() -> new PartidoPoliticoNotFoundException(id));

        actual.setNombre(actualizado.getNombre());
        actual.setSigla(actualizado.getSigla());

        return repository.save(actual);

    }

    public void delete(Long id)
            throws PartidoPoliticoNotFoundException {

        if(!repository.existsById(id)){
            throw new PartidoPoliticoNotFoundException(id);
        }
        repository.deleteById(id);

    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

}
