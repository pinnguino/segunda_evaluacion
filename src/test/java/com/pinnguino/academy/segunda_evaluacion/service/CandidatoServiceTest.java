package com.pinnguino.academy.segunda_evaluacion.service;

import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.repository.CandidatoRepository;
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
class CandidatoServiceTest {

    @Mock
    private CandidatoRepository repository;

    @Mock
    private PartidoPoliticoService partidoPoliticoService;

    @InjectMocks
    private CandidatoService service;

    PartidoPolitico lla, fdt, pn, pi;

    Candidato milei, cristina;

    @BeforeEach
    void setUp() {
        lla =  new PartidoPolitico(1L, "La Libertad Avanza", "LLA");
        fdt = new PartidoPolitico(2L, "Frente de Todos", "FDT");
        pn = new PartidoPolitico(null, "Partido Nulo", "PN");
        pi = new PartidoPolitico(99L, "Partido Inexistente", null);

        milei = new Candidato(1L, "Javier Milei", lla);
        cristina = new Candidato(2L, "Cristina Kirchner", fdt);
    }

    @Test
    @DisplayName("Obtener todos los candidatos")
    void getAll_shouldReturnAll() {
        List<Candidato> candidatos = List.of(new Candidato());
        when(repository.findAll()).thenReturn(candidatos);

        List<Candidato> result = service.getAll();

        assertEquals(candidatos, result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Obtener candidato por id (existe)")
    void getById_whenExists() throws CandidatoNotFoundException {
        Candidato c = milei;
        when(repository.findById(c.getId())).thenReturn(Optional.of(c));

        Candidato result = service.getById(1L);

        assertEquals(c.getId(), result.getId());
        verify(repository).findById(c.getId());
    }

    @Test
    @DisplayName("Obtener candidato por id (no existe)")
    void getById_whenNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CandidatoNotFoundException.class, () ->
                service.getById(99L)
        );
    }

    @Test
    @DisplayName("Crear candidato (ID partido nulo)")
    void create_whenPartidoIdNull() {
        PartidoPolitico partidoNulo = pn;
        Candidato candidato = new Candidato(3L, "Carlos Menem", partidoNulo); // partidoPolitico nulo

        assertThrows(DatosInvalidosException.class, () ->
                service.create(candidato)
        );
        verify(repository, never()).save(any(Candidato.class));
    }

    @Test
    @DisplayName("Crear candidato (partido no existe)")
    void create_whenPartidoNotExists() {
        PartidoPolitico p = lla;
        Candidato candidato = milei;

        when(partidoPoliticoService.exists(p.getId())).thenReturn(false);

        assertThrows(PartidoPoliticoNotFoundException.class, () ->
                service.create(candidato)
        );
        verify(repository, never()).save(any(Candidato.class));
    }

    @Test
    @DisplayName("Crear candidato (datos correctos)")
    void create_whenValid() throws DatosInvalidosException, PartidoPoliticoNotFoundException {
        PartidoPolitico p = fdt;
        Candidato candidato = cristina;
        candidato.setPartidoPolitico(p);

        when(partidoPoliticoService.exists(p.getId())).thenReturn(true);
        when(partidoPoliticoService.getById(p.getId())).thenReturn(p);
        when(repository.save(candidato)).thenReturn(candidato);

        Candidato result = service.create(candidato);

        assertAll(
            () -> assertEquals(candidato, result),
            () -> assertNotNull(result)
        );
        verify(repository).save(candidato);
    }

    @Test
    @DisplayName("Actualizar candidato (no existe)")
    void update_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CandidatoNotFoundException.class, () ->
                service.update(99L, new Candidato())
        );
    }

    @Test
    @DisplayName("Actualizar candidato (ID partido nulo)")
    void update_whenPartidoIsNull() {
        Candidato actual = cristina;
        PartidoPolitico partidoNulo = pn;
        Candidato actualizado = new Candidato(2L, "Cristina Fernández", partidoNulo);

        when(repository.findById(1L)).thenReturn(Optional.of(actual));

        assertThrows(DatosInvalidosException.class, () ->
                service.update(1L, actualizado)
        );
    }

    @Test
    @DisplayName("Actualizar candidato (no existe partido)")
    void update_whenPartidoNotExists() {
        Candidato actual = milei;
        PartidoPolitico p = new PartidoPolitico(99L, "Partido inexistente", null);
        Candidato actualizado = new Candidato(1L, "Javier Milei", p);

        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(partidoPoliticoService.exists(p.getId())).thenReturn(false);

        assertThrows(PartidoPoliticoNotFoundException.class, () ->
                service.update(1L, actualizado)
        );
    }

    @Test
    @DisplayName("Actualizar candidato (datos correctos)")
    void update_whenValid() throws CandidatoNotFoundException, DatosInvalidosException, PartidoPoliticoNotFoundException {
        Candidato actual = cristina;
        PartidoPolitico p = fdt;

        Candidato actualizado = new Candidato(2L, "Cristina Fernández", p);

        when(repository.findById(actual.getId())).thenReturn(Optional.of(actual));
        when(partidoPoliticoService.exists(p.getId())).thenReturn(true);
        when(partidoPoliticoService.getById(p.getId())).thenReturn(p);
        when(repository.save(actual)).thenReturn(actual);

        Candidato result = service.update(actual.getId(), actualizado);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(actualizado, result)
        );
        verify(repository).findById(actual.getId());
        verify(repository).save(actualizado);
    }

    @Test
    @DisplayName("Eliminar candidato (no existe)")
    void delete_whenNotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(CandidatoNotFoundException.class, () ->
                service.delete(99L)
        );
       verify(repository, never()).delete(any(Candidato.class));
    }

    @Test
    @DisplayName("Eliminar candidato (existe)")
    void delete_whenExists() throws CandidatoNotFoundException {
        Candidato c = milei;
        when(repository.existsById(c.getId())).thenReturn(true, false);

        service.delete(c.getId());

        assertFalse(service.exists(c.getId()));
        verify(repository).deleteById(c.getId());
    }

    @Test
    @DisplayName("Existe candidato (caso true y false)")
    void exists_test() {
        when(repository.existsById(5L)).thenReturn(true);
        assertTrue(service.exists(5L));
        when(repository.existsById(6L)).thenReturn(false);
        assertFalse(service.exists(6L));
    }

}
