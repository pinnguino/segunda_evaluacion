package com.pinnguino.academy.segunda_evaluacion.controller;

import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.service.PartidoPoliticoService;
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
@RequestMapping("/api/partidos")
@Tag(name = "Partidos Políticos", description = "API para gestionar partidos políticos")
public class PartidoPoliticoController {

    private final PartidoPoliticoService service;

    @Autowired
    public PartidoPoliticoController(PartidoPoliticoService service) {
        this.service = service;
    }

    @Operation(summary = "Inicializa la lista de partidos políticos", description = "Crea una lista inicial de partidos políticos en el sistema")
    @ApiResponse(responseCode = "201", description = "Partidos políticos creados exitosamente")
    @PostMapping("/init")
    public ResponseEntity<List<PartidoPolitico>> init(@RequestBody List<PartidoPolitico> partidos) {
        return new ResponseEntity<>(service.init(partidos), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene todos los partidos políticos", description = "Retorna una lista de todos los partidos políticos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de partidos políticos recuperada exitosamente")
    @GetMapping
    public ResponseEntity<List<PartidoPolitico>> getPartidosPoliticos() {
        List<PartidoPolitico> partidos = service.getAll();
        return new ResponseEntity<>(partidos, HttpStatus.OK);
    }

    @Operation(summary = "Obtiene un partido político por ID", description = "Retorna un partido político específico basado en su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partido político encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partido político no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PartidoPolitico> getPartidosPoliticos(
            @Parameter(description = "ID del partido político") @PathVariable Long id)
            throws PartidoPoliticoNotFoundException {

        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);

    }

    @Operation(summary = "Crea un nuevo partido político", description = "Crea un nuevo partido político en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Partido político creado exitosamente")
    })
    @PostMapping
    public ResponseEntity<PartidoPolitico> createPartido(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del partido político a crear") 
            @RequestBody PartidoPolitico partido) {
        return new ResponseEntity<>(service.create(partido), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza un partido político", description = "Actualiza los datos de un partido político existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partido político actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partido político no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PartidoPolitico> updatePartido(
            @Parameter(description = "ID del partido político") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del partido político") 
            @RequestBody PartidoPolitico partido)
            throws PartidoPoliticoNotFoundException {
        return new ResponseEntity<>(service.update(id, partido), HttpStatus.OK);
    }

    @Operation(summary = "Elimina un partido político", description = "Elimina un partido político del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Partido político eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Partido político no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartido(
            @Parameter(description = "ID del partido político") @PathVariable Long id) 
            throws PartidoPoliticoNotFoundException {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
