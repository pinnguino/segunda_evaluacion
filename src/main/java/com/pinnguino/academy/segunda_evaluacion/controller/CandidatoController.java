package com.pinnguino.academy.segunda_evaluacion.controller;

import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.service.CandidatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
@Tag(name = "Candidatos", description = "API para gestionar candidatos políticos")
public class CandidatoController {

    private CandidatoService service;

    @Autowired
    public CandidatoController(CandidatoService service) {
        this.service = service;
    }

    @Operation(summary = "Inicializa la lista de candidatos", description = "Crea una lista inicial de candidatos en el sistema")
    @ApiResponse(responseCode = "201", description = "Candidatos creados exitosamente")
    @PostMapping("/init")
    public ResponseEntity<List<Candidato>> init(@RequestBody List<Candidato> candidatos) {
        return new ResponseEntity<>(service.init(candidatos), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene todos los candidatos", description = "Retorna una lista de todos los candidatos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de candidatos recuperada exitosamente")
    @GetMapping
    public ResponseEntity<List<Candidato>> getCandidatos() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Obtiene un candidato por ID", description = "Retorna un candidato específico basado en su ID")
    @ApiResponse(responseCode = "200", description = "Candidato encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Candidato no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Candidato> getCandidatoById(@Parameter(description = "ID del candidato") @PathVariable Long id)
            throws CandidatoNotFoundException {

        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);

    }

    @Operation(summary = "Crea un nuevo candidato", description = "Crea un nuevo candidato en el sistema")
    @ApiResponse(responseCode = "201", description = "Candidato creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para el candidato")
    @ApiResponse(responseCode = "404", description = "Partido político no encontrado")
    @PostMapping
    public ResponseEntity<Candidato> createCandidato(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del candidato a crear") 
            @RequestBody Candidato candidato)
            throws PartidoPoliticoNotFoundException, DatosInvalidosException {
        return new ResponseEntity<>(service.create(candidato), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza un candidato", description = "Actualiza los datos de un candidato existente")
    @ApiResponse(responseCode = "200", description = "Candidato actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para el candidato")
    @ApiResponse(responseCode = "404", description = "Candidato o partido político no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Candidato> updateCandidato(
            @Parameter(description = "ID del candidato") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del candidato") 
            @RequestBody Candidato candidato)
            throws CandidatoNotFoundException, PartidoPoliticoNotFoundException, DatosInvalidosException {
        return new ResponseEntity<>(service.update(id, candidato), HttpStatus.OK);
    }

    @Operation(summary = "Elimina un candidato", description = "Elimina un candidato del sistema")
    @ApiResponse(responseCode = "204", description = "Candidato eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Candidato no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidato(
            @Parameter(description = "ID del candidato") @PathVariable Long id)
            throws CandidatoNotFoundException {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
