package com.pinnguino.academy.segunda_evaluacion.service;

import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.repository.PartidoPoliticoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidoPoliticoServiceTest {

    @Mock
    private PartidoPoliticoRepository repository;

    @InjectMocks
    private PartidoPoliticoService service;

    // Instancias para los tests
    PartidoPolitico partidoAzul, partidoRojo, partidoVioleta;

    @BeforeEach
    void setUp() {
        partidoAzul = new PartidoPolitico(1L, "Partido Azul", "PA");
        partidoRojo = new PartidoPolitico(2L, "Partido Rojo", "PR");
        partidoVioleta = new PartidoPolitico(10L, "Partido Violeta", "PV");
    }

    @Test
    @DisplayName("Test datos de ejemplo")
    void init_test() {
        List<PartidoPolitico> partidos = List.of(partidoAzul, partidoRojo);

        when(repository.saveAll(partidos)).thenReturn(partidos);

        List<PartidoPolitico> result = service.init(partidos);

        assertEquals(partidos, result);
        verify(repository).saveAll(partidos);
    }

    @Test
    @DisplayName("Obtener todos los partidos")
    void getAll_test() {
        List<PartidoPolitico> partidos = List.of(new PartidoPolitico());
        when(repository.findAll()).thenReturn(partidos);

        List<PartidoPolitico> result = service.getAll();

        assertEquals(partidos, result);
    }

    @Test
    @DisplayName("Obtener partido por ID (existe)")
    void getById_whenExists_test() throws PartidoPoliticoNotFoundException {
        PartidoPolitico p = partidoAzul;
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        PartidoPolitico result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Obtener partido por ID (no existe)")
    void getById_whenNotExists_test() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Exception e = assertThrows(PartidoPoliticoNotFoundException.class, () ->
            service.getById(2L)
        );

        assertEquals("Partido con ID " + 2L + " no encontrado.", e.getMessage());
    }

    @Test
    @DisplayName("Crear partido")
    void create_test() {
        PartidoPolitico p = partidoAzul;
        when(repository.save(p)).thenReturn(p);

        PartidoPolitico result = service.create(p);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(p, result)
        );
        verify(repository).save(p);
    }

    @Test
    @DisplayName("Actualizar partido (no existe)")
    void update_whenNotExists() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        Exception e = assertThrows(PartidoPoliticoNotFoundException.class, () ->
            service.update(10L, partidoVioleta)
        );

        assertEquals("Partido con ID " + 10L + " no encontrado.", e.getMessage());

        verify(repository, never())
                .save(any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Actualizar partido (existe)")
    void update_whenExists() throws PartidoPoliticoNotFoundException {
        PartidoPolitico actual = partidoAzul;

        PartidoPolitico actualizado = new PartidoPolitico(partidoAzul.getId(), "Partido Naranja", "PN");

        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(repository.save(any(PartidoPolitico.class))).thenReturn(actual);

        PartidoPolitico result = service.update(partidoAzul.getId(), actualizado);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(actualizado, result)
        );
        verify(repository).save(actualizado);
    }

    @Test
    @DisplayName("Borrar partido (no existe)")
    void delete_whenNotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        Exception e = assertThrows(PartidoPoliticoNotFoundException.class, () ->
            service.delete(99L)
        );

        assertEquals("Partido con ID " + 99L + " no encontrado.", e.getMessage());

        verify(repository, never())
            .deleteById(anyLong());
    }

    @Test
    @DisplayName("Borrar partido (existe)")
    void delete_whenExists() throws PartidoPoliticoNotFoundException {
        // Simulate repository: exists returns true before delete and false after
        when(repository.existsById(2L)).thenReturn(true, false);

        service.delete(2L);

        // After the delete call we expect exists to be false (simulated)
        assertFalse(service.exists(2L));
        verify(repository).deleteById(2L);
    }

    @Test
    @DisplayName("Existe partido en la base de datos")
    void exists_value() {
        when(repository.existsById(5L)).thenReturn(true);
        assertTrue(service.exists(5L));

        when(repository.existsById(6L)).thenReturn(false);
        assertFalse(service.exists(6L));
    }

}
