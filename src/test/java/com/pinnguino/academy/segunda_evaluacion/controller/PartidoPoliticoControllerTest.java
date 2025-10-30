package com.pinnguino.academy.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.handler.GlobalExceptionHandler;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.service.PartidoPoliticoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PartidoPoliticoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PartidoPoliticoService service;

    @InjectMocks
    private PartidoPoliticoController controller;

    PartidoPolitico partidoAzul, partidoRojo;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        partidoAzul = new PartidoPolitico(1L, "Partido Azul", "PA");
        partidoRojo = new PartidoPolitico(2L, "Partido Rojo", "PR");
    
    }

    @Test
    @DisplayName("Request inicializar con datos de prueba")
    void init_test() throws Exception {
        List<PartidoPolitico> partidos = List.of(partidoAzul, partidoRojo);
        when(service.init(partidos)).thenReturn(partidos);

        mockMvc.perform(post("/api/partidos/init")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partidos)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).init(partidos);
    }

    @Test
    @DisplayName("Request obtener todos los partidos")
    void getAll_test() throws Exception {
        List<PartidoPolitico> partidos = List.of(partidoAzul, partidoRojo);
        when(service.getAll()).thenReturn(partidos);

        mockMvc.perform(get("/api/partidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).getAll();
    }

    @Test
    @DisplayName("Request crear un partido")
    void create_test() throws Exception {
        PartidoPolitico p = partidoRojo;
        when(service.create(p)).thenReturn(p);

        mockMvc.perform(post("/api/partidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).create(p);
    }

    @Test
    @DisplayName("Request obtener partido por ID (existe)")
    void getById_whenExists() throws Exception {
        PartidoPolitico p = partidoAzul;
        Long idPartido = p.getId();
        when(service.getById(idPartido)).thenReturn(p);

        mockMvc.perform(get("/api/partidos/" + idPartido.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).getById(1L);
    }

    @Test
    @DisplayName("Request obtener partido por ID (no existe)")
    void getById_whenNotExists_shouldReturnNotFound() throws Exception {

        Long idPartido = partidoRojo.getId();

        when(service.getById(idPartido)).thenThrow(new PartidoPoliticoNotFoundException(idPartido));

        mockMvc.perform(get("/api/partidos/" +  idPartido.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Partido con ID " + idPartido + " no encontrado."));

        verify(service).getById(idPartido);
    }

    @Test
    @DisplayName("Request actualizar partido (existe)")
    void update_whenExists_shouldReturnOk() throws Exception {

        Long idPartido = partidoAzul.getId();
        PartidoPolitico actualizado = new PartidoPolitico(idPartido, "Partido Negro", "PN");
        when(service.update(idPartido, actualizado)).thenReturn(actualizado);

        mockMvc.perform(put("/api/partidos/" +  idPartido.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(service).update(eq(idPartido), any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Request actualizar partido (no existe)")
    void update_whenNotExists_shouldReturnNotFound() throws Exception {
        PartidoPolitico actualizado = new PartidoPolitico(10L, "Partido X", "PX");
        when(service.update(eq(10L), any(PartidoPolitico.class))).thenThrow(new PartidoPoliticoNotFoundException(10L));

        mockMvc.perform(put("/api/partidos/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Partido con ID 10 no encontrado."));

        verify(service).update(eq(10L), any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Request eliminar partido (existe)")
    void delete_whenExists() throws Exception {
        Long idPartido = partidoRojo.getId();
        doNothing().when(service).delete(idPartido);

        mockMvc.perform(delete("/api/partidos/" + idPartido.toString()))
                .andExpect(status().isNoContent());

        verify(service).delete(idPartido);
    }

    @Test
    @DisplayName("Request eliminar partido (no existe)")
    void delete_whenNotExists() throws Exception {
        Long idPartido = 99L;
        doThrow(new PartidoPoliticoNotFoundException(idPartido)).when(service).delete(idPartido);

        mockMvc.perform(delete("/api/partidos/" + idPartido.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Partido con ID " + idPartido.toString() + " no encontrado."));

        verify(service).delete(idPartido);
    }

}