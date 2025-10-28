package com.pinnguino.academy.segunda_evaluacion.controller;

import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNoEncontradoException;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    private CandidatoService service;

    @Autowired
    public CandidatoController(CandidatoService service) {
        this.service = service;
    }

    @PostMapping("/init")
    public ResponseEntity<List<Candidato>> init(@RequestBody List<Candidato> candidatos) {
        return new ResponseEntity<>(service.init(candidatos), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Candidato>> getCandidatos() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidato> getCandidatoById(@PathVariable Long id)
            throws CandidatoNotFoundException {

        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Candidato> createCandidato(@RequestBody Candidato candidato)
            throws PartidoPoliticoNoEncontradoException, DatosInvalidosException {
        return new ResponseEntity<>(service.create(candidato), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidato> updateCandidato(@PathVariable Long id, @RequestBody Candidato candidato)
            throws CandidatoNotFoundException, PartidoPoliticoNoEncontradoException, DatosInvalidosException {

        return new ResponseEntity<>(service.update(id, candidato), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidato(@PathVariable Long id)
            throws CandidatoNotFoundException {

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
