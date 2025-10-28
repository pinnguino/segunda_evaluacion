package com.pinnguino.academy.segunda_evaluacion.service;

import com.pinnguino.academy.segunda_evaluacion.dto.CantidadVotosDTO;
import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import com.pinnguino.academy.segunda_evaluacion.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotoService {

    private final VotoRepository repository;
    private final CandidatoService candidatoService;
    private final PartidoPoliticoService partidoPoliticoService;

    @Autowired
    public VotoService(VotoRepository repository, CandidatoService candidatoService, PartidoPoliticoService partidoPoliticoService) {
        this.repository = repository;
        this.candidatoService = candidatoService;
        this.partidoPoliticoService = partidoPoliticoService;
    }

    public List<Voto> init(List<Voto> votos) {
        return repository.saveAll(votos);
    }

    public List<Voto> getAll() {
        return repository.findAll();
    }

    public Voto create(Voto voto)
            throws CandidatoNotFoundException, PartidoPoliticoNotFoundException, DatosInvalidosException {

        Long idCandidato = voto.getCandidato().getId();
        Long idPartidoPolitico = voto.getCandidato().getPartidoPolitico().getId();

        if(idCandidato == null ||  idPartidoPolitico == null) {
            throw new DatosInvalidosException();
        }
        else if(!candidatoService.exists(idCandidato)) {
            throw new CandidatoNotFoundException(idCandidato);
        }
        else if(!partidoPoliticoService.exists(idPartidoPolitico)) {
            throw new PartidoPoliticoNotFoundException(idPartidoPolitico);
        }

        return repository.save(voto);

    }

    public CantidadVotosDTO votosPorCandidato(Long idCandidato)
            throws CandidatoNotFoundException {

        if(candidatoService.exists(idCandidato)) {
            String nombreCandidato = candidatoService.getById(idCandidato).getNombreCompleto();
            int cantidadVotos = repository.findByCandidato_Id(idCandidato).size();
            return new CantidadVotosDTO("Candidato", nombreCandidato, cantidadVotos);
        }

        throw new CandidatoNotFoundException(idCandidato);

    }

    public CantidadVotosDTO votosPorPartido(Long idPartido)
            throws PartidoPoliticoNotFoundException {

        if(partidoPoliticoService.exists(idPartido)) {
            String nombrePartido = partidoPoliticoService.getById(idPartido).getNombre();
            int cantidadVotos = repository.findByCandidato_PartidoPolitico_Id(idPartido).size();

            return new CantidadVotosDTO("Partido Político",  nombrePartido, cantidadVotos);
        }

        throw new PartidoPoliticoNotFoundException(idPartido);

    }

}
