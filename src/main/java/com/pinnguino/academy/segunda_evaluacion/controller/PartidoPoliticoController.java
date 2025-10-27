package com.pinnguino.academy.segunda_evaluacion.controller;

import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNoEncontradoException;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.service.PartidoPoliticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoPoliticoController {

    private final PartidoPoliticoService service;

    @Autowired
    public PartidoPoliticoController(PartidoPoliticoService service) {
        this.service = service;
    }

    @GetMapping("/init")
    public ResponseEntity<List<PartidoPolitico>> init(@RequestBody List<PartidoPolitico> partidos) {
        return new ResponseEntity<>(service.init(partidos), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PartidoPolitico>> getPartidosPoliticos() {
        List<PartidoPolitico> partidos = service.getAll();

        return new ResponseEntity<>(partidos, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PartidoPolitico> getPartidosPoliticos(@PathVariable Long id) throws PartidoPoliticoNoEncontradoException {
        PartidoPolitico partido = service.getById(id);
        return new ResponseEntity<>(partido, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PartidoPolitico> createPartido(@RequestBody PartidoPolitico partido) {
        return new ResponseEntity<>(service.create(partido), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidoPolitico> updatePartido(@PathVariable Long id, @RequestBody PartidoPolitico partido) throws PartidoPoliticoNoEncontradoException {
        PartidoPolitico actual = service.getById(id);

        return new ResponseEntity<>(service.update(id, partido), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartido(@PathVariable Long id) throws PartidoPoliticoNoEncontradoException {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
