package com.pinnguino.academy.segunda_evaluacion.service;

import com.pinnguino.academy.segunda_evaluacion.dto.CantidadVotosDTO;
import com.pinnguino.academy.segunda_evaluacion.exception.CandidatoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.exception.DatosInvalidosException;
import com.pinnguino.academy.segunda_evaluacion.exception.PartidoPoliticoNotFoundException;
import com.pinnguino.academy.segunda_evaluacion.model.Candidato;
import com.pinnguino.academy.segunda_evaluacion.model.PartidoPolitico;
import com.pinnguino.academy.segunda_evaluacion.model.Voto;
import com.pinnguino.academy.segunda_evaluacion.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository repository;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private PartidoPoliticoService partidoPoliticoService;

    @InjectMocks
    private VotoService service;

    PartidoPolitico partido, partidoNulo;
    Candidato candidato, candidatoNulo;
    Voto voto;

    @BeforeEach
    void setUp() {
        partido = new PartidoPolitico(1L, "Partido MVC", "PMVC");
        candidato = new Candidato(1L, "Springo Bootez", partido);
        voto = new Voto(1L, candidato, LocalDateTime.now());
        partidoNulo = new PartidoPolitico(null, "Partido Nulo", "PN");
        candidatoNulo = new Candidato(null, "Candidato Nulo", partidoNulo);
    }

    @Test
    void getAll_test() {
        List<Voto> votos = List.of(new Voto());
        when(repository.findAll()).thenReturn(votos);

        List<Voto> result = service.getAll();

        assertEquals(votos, result);
        verify(repository).findAll();
    }

    @Test
    void create_whenIdsNull() {
        Candidato c = candidatoNulo;
        Voto v = new Voto(2L, c, LocalDateTime.now());


        assertThrows(DatosInvalidosException.class, () ->
                service.create(v)
        );
        verify(repository, never()).save(v);
    }

    @Test
    void create_whenCandidatoNotExists() {
        Candidato c = candidato;
        c.setId(99L);
        Voto v = voto;

        when(candidatoService.exists(c.getId())).thenReturn(false);

        assertThrows(CandidatoNotFoundException.class, () ->
                service.create(v)
        );

        verify(repository, never()).save(any(Voto.class));
    }

    @Test
    void create_whenPartidoNotExists() {
        Candidato c = candidato;
        PartidoPolitico p = partido;
        p.setId(99L);
        Voto v= voto;

        when(candidatoService.exists(c.getId())).thenReturn(true);
        when(partidoPoliticoService.exists(p.getId())).thenReturn(false);

        assertThrows(PartidoPoliticoNotFoundException.class, () ->
                service.create(v)
        );
        verify(repository, never()).save(any(Voto.class));
    }

    @Test
    void create_whenValid() throws CandidatoNotFoundException, PartidoPoliticoNotFoundException, DatosInvalidosException {
        Candidato c = candidato;
        PartidoPolitico p = partido;
        Voto v = voto;

        when(candidatoService.exists(c.getId())).thenReturn(true);
        when(partidoPoliticoService.exists(p.getId())).thenReturn(true);
        when(repository.save(v)).thenReturn(v);

        Voto result = service.create(voto);

        assertNotNull(result);
        verify(repository).save(voto);
    }

    @Test
    void votosPorCandidato_whenExists() throws CandidatoNotFoundException {
        Candidato c = candidato;
        when(candidatoService.exists(c.getId())).thenReturn(true);
        when(candidatoService.getById(c.getId())).thenReturn(c);
        when(repository.findByCandidato_Id(c.getId())).thenReturn(List.of(new Voto(), new Voto()));

        CantidadVotosDTO dto = service.votosPorCandidato(c.getId());

        assertEquals("Candidato", dto.getTipoEntidad());
        assertEquals(c.getNombreCompleto(), dto.getNombre());
        assertEquals(2, dto.getCantidadVotos());

        verify(repository).findByCandidato_Id(c.getId());
    }

    @Test
    void votosPorCandidato_whenNotExists() {
        Candidato c = candidato;
        c.setId(99L);
        when(candidatoService.exists(c.getId())).thenReturn(false);

        assertThrows(CandidatoNotFoundException.class, () ->
                service.votosPorCandidato(c.getId())
        );
        verify(repository, never()).findByCandidato_Id(anyLong());
    }

    @Test
    void votosPorPartido_whenExists() throws PartidoPoliticoNotFoundException {
        PartidoPolitico p = partido;
        when(partidoPoliticoService.exists(p.getId())).thenReturn(true);
        when(partidoPoliticoService.getById(p.getId())).thenReturn(p);
        when(repository.findByCandidato_PartidoPolitico_Id(p.getId())).thenReturn(List.of(new Voto()));

        CantidadVotosDTO dto = service.votosPorPartido(p.getId());

        assertEquals("Partido Político", dto.getTipoEntidad());
        assertEquals(p.getNombre(), dto.getNombre());
        assertEquals(1, dto.getCantidadVotos());

        verify(repository).findByCandidato_PartidoPolitico_Id(p.getId());
    }

    @Test
    void votosPorPartido_whenNotExists() {
        PartidoPolitico p = partido;
        p.setId(99L);
        when(partidoPoliticoService.exists(p.getId())).thenReturn(false);

        assertThrows(PartidoPoliticoNotFoundException.class, () ->
                service.votosPorPartido(p.getId())
        );

        verify(repository, never()).findByCandidato_PartidoPolitico_Id(anyLong());
    }

}
