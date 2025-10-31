package com.pinnguino.academy.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.handler.GlobalExceptionHandler;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.service.CandidatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CandidatoControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CandidatoService candidatoService;

    @InjectMocks
    private CandidatoController controller;

    PartidoPolitico lla, fdt, pn, pi;

    Candidato milei, cristina, espert;

    @BeforeEach
    void setup() {
        // Wire the global exception handler so exceptions map to HTTP statuses in tests
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        lla =  new PartidoPolitico(1L, "La Libertad Avanza", "LLA");
        fdt = new PartidoPolitico(2L, "Frente de Todos", "FDT");
        pn = new PartidoPolitico(null, "Partido Nulo", "PN");
        pi = new PartidoPolitico(99L, "Partido Inexistente", null);

        milei = new Candidato(1L, "Javier Milei", lla);
        cristina = new Candidato(2L, "Cristina Kirchner", fdt);
        espert = new Candidato(99L, null, null);
    }

    @Test
    void getAll_shouldReturnOk() throws Exception {
        when(candidatoService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/candidatos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(candidatoService).getAll();
    }

    @Test
    void getById_whenFound() throws Exception {
        Candidato c = milei;
        when(candidatoService.getById(c.getId())).thenReturn(c);

        mockMvc.perform(get("/api/candidatos/" + c.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(candidatoService).getById(c.getId());
    }

    @Test
    void getById_whenMissing() throws Exception {
        Candidato c = espert;
        when(candidatoService.getById(c.getId())).thenThrow(new CandidatoNotFoundException(c.getId()));

        mockMvc.perform(get("/api/candidatos/" + c.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Candidato con ID " + c.getId().toString() + " no encontrado."));

        verify(candidatoService).getById(c.getId());
    }

    @Test
    void createCandidato_whenValid() throws Exception {
        Candidato c = cristina;
        when(candidatoService.create(c)).thenReturn(c);

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(candidatoService).create(cristina);
    }

    @Test
    void createCandidato_whenPartidoMissing() throws Exception {

        Candidato c = espert;
        when(candidatoService.create(c)).thenThrow(new PartidoPoliticoNotFoundException(c.getId()));

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Partido con ID " + 99L + " no encontrado."));

        verify(candidatoService).create(c);
    }

    @Test
    void createCandidato_whenDatosInvalidos() throws Exception {
        Candidato c = cristina;
        when(candidatoService.create(c)).thenThrow(new DatosInvalidosException());

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El objeto contiene datos inválidos o nulos."));

        verify(candidatoService).create(c);
    }

    @Test
    void updateCandidato_whenValid() throws Exception {
        Candidato actual = cristina;
        PartidoPolitico p = fdt;
        Candidato actualizado = new Candidato(actual.getId(), "Cristina Fernández", p);
        when(candidatoService.update(actual.getId(),  actualizado)).thenReturn(actualizado);

        mockMvc.perform(put("/api/candidatos/" + actual.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(actualizado)))
                    .andExpect(status().isOk());

        verify(candidatoService).update(actual.getId(), actualizado);
    }

    @Test
    void updateCandidato_whenMissing() throws Exception {
        Candidato c = espert;
        when(candidatoService.update(c.getId(), c)).thenThrow(new CandidatoNotFoundException(c.getId()));

        mockMvc.perform(put("/api/candidatos/" + c.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Candidato con ID " + c.getId().toString() + " no encontrado."));

        verify(candidatoService).update(c.getId(), c);
    }

    @Test
    void deleteCandidato_shouldReturnNoContent() throws Exception {
        Candidato c = milei;
        doNothing().when(candidatoService).delete(c.getId());

        mockMvc.perform(delete("/api/candidatos/" + c.getId().toString()))
                .andExpect(status().isNoContent());

        verify(candidatoService).delete(c.getId());
    }

    @Test
    void deleteCandidato_whenMissing() throws Exception {
        Candidato c = espert;
        doThrow(new CandidatoNotFoundException(c.getId())).when(candidatoService).delete(c.getId());

        mockMvc.perform(delete("/api/candidatos/" + c.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Candidato con ID " + c.getId().toString() + " no encontrado."));

        verify(candidatoService).delete(c.getId());
    }

}
