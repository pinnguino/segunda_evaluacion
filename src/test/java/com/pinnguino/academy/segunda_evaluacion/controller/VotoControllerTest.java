package com.pinnguino.academy.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pinnguino.academy.segunda_evaluacion.dto.CantidadVotosDTO;
import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.handler.GlobalExceptionHandler;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import com.pinnguino.academy.segunda_evaluacion.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VotoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private VotoService service;

    @InjectMocks
    private VotoController controller;

    PartidoPolitico partido, partidoNulo;
    Candidato candidato, candidatoNulo;
    Voto voto;
    CantidadVotosDTO cantVotosCandidato;
    CantidadVotosDTO cantVotosPartido;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        partido = new PartidoPolitico(1L, "Partido MVC", "PMVC");
        candidato = new Candidato(1L, "Springo Bootez", partido);
        voto = new Voto(1L, candidato, LocalDateTime.now());
        partidoNulo = new PartidoPolitico(null, "Partido Nulo", "PN");
        candidatoNulo = new Candidato(null, "Candidato Nulo", partidoNulo);
        cantVotosCandidato = new CantidadVotosDTO("Candidato", "Springo Bootez", 10);
        cantVotosCandidato = new CantidadVotosDTO("Candidato", "Partido MVC", 4);
    }

    @Test
    void findAll_test() throws Exception {
        when(service.getAll()).thenReturn(List.of(voto));

        mockMvc.perform(get("/api/votos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).getAll();
    }

    @Test
    void createVoto_whenValid() throws Exception {
        Voto v = voto;
        when(service.create(v)).thenReturn(v);

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v))) // Jackson deserializa el objeto y crea una nueva instancia.
                // No entiendo por qué acá pasa, en los otros tests no ocurre. La única diferencia es el constructor del objectMapper, para que acepte el formato de las fechas de emisión.
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).create(v);
    }

    @Test
    void createVoto_whenCandidatoMissing() throws Exception {
        Voto v = voto;
        when(service.create(v)).thenThrow(new CandidatoNotFoundException(v.getId()));

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Candidato con ID " + v.getId().toString() + " no encontrado."));
    }

    @Test
    void createVoto_whenPartidoMissing() throws Exception {
        Voto v = voto;
        when(service.create(v)).thenThrow(new PartidoPoliticoNotFoundException(v.getId()));

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Partido con ID " + v.getId().toString() + " no encontrado."));
    }

    @Test
    void createVoto_shouldReturnBadRequest_whenDatosInvalidos() throws Exception {
        when(service.create(any(Voto.class))).thenThrow(new com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException());

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El objeto contiene datos inválidos o nulos."));
    }

    @Test
    void votesByCandidato_whenValid() throws Exception {
        CantidadVotosDTO dto = cantVotosCandidato;
        Candidato c = candidato;

        when(service.votosPorCandidato(c.getId())).thenReturn(dto);

        mockMvc.perform(get("/api/votos/cantVotos/candidato/" + c.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).votosPorCandidato(c.getId());
    }

    @Test
    void votesByCandidato_whenMissing() throws Exception {
        Candidato c = candidato;
        candidato.setId(99L);
        when(service.votosPorCandidato(c.getId())).thenThrow(new CandidatoNotFoundException(c.getId()));

        mockMvc.perform(get("/api/votos/cantVotos/candidato/" + c.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Candidato con ID " + c.getId().toString() + " no encontrado."));

        verify(service).votosPorCandidato(c.getId());
    }

    @Test
    void votesByPartido_whenValid() throws Exception {
        PartidoPolitico p = partido;
        CantidadVotosDTO dto = cantVotosPartido;
        when(service.votosPorPartido(p.getId())).thenReturn(dto);

        mockMvc.perform(get("/api/votos/cantVotos/partido/" + p.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).votosPorPartido(p.getId());
    }

    @Test
    void votesByPartido_whenMissing() throws Exception {
        PartidoPolitico p = partido;
        p.setId(99L);
        when(service.votosPorPartido(p.getId())).thenThrow(new PartidoPoliticoNotFoundException(p.getId()));

        mockMvc.perform(get("/api/votos/cantVotos/partido/" +  p.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Partido con ID " + p.getId().toString() + " no encontrado."));

        verify(service).votosPorPartido(p.getId());
    }

}
