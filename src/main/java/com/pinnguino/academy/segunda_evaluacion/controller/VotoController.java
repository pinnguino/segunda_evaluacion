package com.pinnguino.academy.segunda_evaluacion.controller;

import com.pinnguino.academy.segunda_evaluacion.dto.CantidadVotosDTO;
import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import com.pinnguino.academy.segunda_evaluacion.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

    private VotoService service;

    @Autowired
    public void setVotoService(VotoService service) {
        this.service = service;
    }

    @PostMapping("/init")
    public ResponseEntity<List<Voto>> init(@RequestBody List<Voto> votos) {
        return new ResponseEntity<>(service.init(votos), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Voto>> findAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Voto> createVoto(@RequestBody Voto voto)
            throws CandidatoNotFoundException, PartidoPoliticoNotFoundException, DatosInvalidosException {
        return new ResponseEntity<>(service.create(voto), HttpStatus.CREATED);
    }

    @GetMapping("/cantVotos/candidato/{id}")
    public ResponseEntity<CantidadVotosDTO> votesByCandidato(@PathVariable Long id) throws CandidatoNotFoundException {
        return new ResponseEntity<>(service.votosPorCandidato(id), HttpStatus.OK);
    }

    @GetMapping("/cantVotos/partido/{id}")
    public ResponseEntity<CantidadVotosDTO> votesByPartido(@PathVariable Long id) throws PartidoPoliticoNotFoundException {
        return new ResponseEntity<>(service.votosPorPartido(id), HttpStatus.OK);
    }

}
