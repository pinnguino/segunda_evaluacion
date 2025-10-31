package com.pinnguino.academy.segunda_evaluacion.controller;

import com.pinnguino.academy.segunda_evaluacion.dto.CantidadVotosDTO;
import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import com.pinnguino.academy.segunda_evaluacion.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votos")
@Tag(name = "Votos", description = "API para gestionar votos de los candidatos")
public class VotoController {

    private VotoService service;

    @Autowired
    public void setVotoService(VotoService service) {
        this.service = service;
    }

    @Operation(summary = "Inicializa la lista de votos", description = "Crea una lista inicial de votos en el sistema")
    @ApiResponse(responseCode = "201", description = "Votos creados exitosamente")
    @PostMapping("/init")
    public ResponseEntity<List<Voto>> init(@RequestBody List<Voto> votos) {
        return new ResponseEntity<>(service.init(votos), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene todos los votos", description = "Retorna una lista de todos los votos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de votos recuperada exitosamente")
    @GetMapping
    public ResponseEntity<List<Voto>> findAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Registra un nuevo voto", description = "Crea un nuevo voto en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Voto registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos para el voto"),
        @ApiResponse(responseCode = "404", description = "Candidato o partido político no encontrado")
    })
    @PostMapping
    public ResponseEntity<Voto> createVoto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del voto a registrar") 
            @RequestBody Voto voto)
            throws CandidatoNotFoundException, PartidoPoliticoNotFoundException, DatosInvalidosException {
        return new ResponseEntity<>(service.create(voto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene cantidad de votos por candidato", 
              description = "Retorna la cantidad de votos que tiene un candidato específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cantidad de votos recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Candidato no encontrado")
    })
    @GetMapping("/cantVotos/candidato/{id}")
    public ResponseEntity<CantidadVotosDTO> votesByCandidato(
            @Parameter(description = "ID del candidato") @PathVariable Long id) 
            throws CandidatoNotFoundException {
        return new ResponseEntity<>(service.votosPorCandidato(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtiene cantidad de votos por partido político", 
              description = "Retorna la cantidad de votos que tiene un partido político específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cantidad de votos recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partido político no encontrado")
    })
    @GetMapping("/cantVotos/partido/{id}")
    public ResponseEntity<CantidadVotosDTO> votesByPartido(
            @Parameter(description = "ID del partido político") @PathVariable Long id) 
            throws PartidoPoliticoNotFoundException {
        return new ResponseEntity<>(service.votosPorPartido(id), HttpStatus.OK);
    }

}
