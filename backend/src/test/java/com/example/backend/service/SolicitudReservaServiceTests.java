package com.example.backend.service;

import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.model.Reserva;
import com.example.backend.model.SolicitudReserva;
import com.example.backend.model.Usuario;
import com.example.backend.repository.SolicitudReservaRepository;

@ExtendWith(MockitoExtension.class)
public class SolicitudReservaServiceTests {
    
    @Mock
    private SolicitudReservaRepository solicitudRepository;

    @InjectMocks
    private SolicitudReservaService solicitudService;

    @Test
    void testCrearSolicitudGuardadoExitoso(){        //Arrange
        SolicitudReserva solicitud = new SolicitudReserva();

        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);
        //Act
        SolicitudReserva solicitudGuardada = solicitudService.crearSolicitud(solicitud);
        //Assert
        assertNotNull(solicitudGuardada, "verificacion");
        assertEquals(solicitud, solicitudGuardada, "verificacion");

        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void testObtenerTodasExistenSolicitudes(){        //Arrange
        SolicitudReserva sol1 = new SolicitudReserva();
        SolicitudReserva sol2 = new SolicitudReserva();

        List <SolicitudReserva> solicitudes = List.of(sol1, sol2);

        when(solicitudRepository.findAll()).thenReturn(solicitudes);

        //Act
        List<SolicitudReserva> resultado = solicitudService.obtenerTodas();

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(solicitudes.size(), resultado.size(), "verificacion");
        assertEquals(solicitudes.get(1), resultado.get(1), "verificacion");

        verify(solicitudRepository).findAll();
    }

    @Test
    void testObtenerTodasNoExistenSolicitudes(){        //Arrange
        when(solicitudRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <SolicitudReserva> resultado = solicitudService.obtenerTodas();

        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(solicitudRepository).findAll();
    }

    @Test
    void testObtenerPorIdExisteSolicitud(){        //Arrange
        Long id = 1L;
        SolicitudReserva sol = new SolicitudReserva();
        sol.setIdSolicitud(id);

        when(solicitudRepository.findById(id)).thenReturn(Optional.of(sol));
        //Act
        Optional <SolicitudReserva> resultado = solicitudService.obtenerPorId(id);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(id, resultado.get().getIdSolicitud(), "verificacion");

        verify(solicitudRepository).findById(id);
    }

    @Test
    void testObtenerPorIdNoExisteSolicitud(){        //Arrange
        Long id = 1L;

        when(solicitudRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <SolicitudReserva> resultado = solicitudService.obtenerPorId(id);

        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(solicitudRepository).findById(id);
    }

    @Test
    void testActualizarSolicitudExisteSolicitud(){        //Arrange
        Long id = 1L;
        Reserva res = new Reserva();
        Reserva resActualizada = new Reserva();
        SolicitudReserva solOriginal = new SolicitudReserva();
        solOriginal.setIdSolicitud(id);
        solOriginal.setReserva(res);
        SolicitudReserva solActualizada = new SolicitudReserva();
        solActualizada.setReserva(resActualizada);

        when(solicitudRepository.findById(id)).thenReturn(Optional.of(solOriginal));
        when(solicitudRepository.save(any(SolicitudReserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        SolicitudReserva solicitudActualizada = solicitudService.actualizarSolicitud(id, solActualizada);

        //Assert
        assertNotNull(solicitudActualizada, "verificacion");
        assertEquals(solOriginal.getIdSolicitud(), solicitudActualizada.getIdSolicitud(), "verificacion");
        assertEquals(solActualizada.getReserva(), solicitudActualizada.getReserva(), "verificacion");

        verify(solicitudRepository).findById(id);
        verify(solicitudRepository).save(any(SolicitudReserva.class));
    }

    @Test
    void testActualizarSolicitudNoExisteSolicitud(){        //Arrange
        Long id = 1L;
        SolicitudReserva nuevaSolicitud = new SolicitudReserva();
        when(solicitudRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            solicitudService.actualizarSolicitud(id, nuevaSolicitud);
        }, "verificacion");

        assertEquals("Solicitud no encontrada", exception.getMessage(), "verificacion");
        verify(solicitudRepository, never()).save(any(SolicitudReserva.class));

    }

    @Test
    void testEliminarSolicitudExisteSolicitud(){        //Arrange
        Long id = 1L;

        when(solicitudRepository.existsById(id)).thenReturn(true);
        //Act
        solicitudService.eliminarSolicitud(id);

        //Assert
        verify(solicitudRepository).existsById(id);
        verify(solicitudRepository).deleteById(id);

        verifyNoMoreInteractions(solicitudRepository);

    }

    @Test
    void testEliminarSolicitudNoExisteSolicitud(){        //Arrange
        Long id = 1L;

        when(solicitudRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            solicitudService.eliminarSolicitud(id);
        }, "verificacion");

        assertEquals("Solicitud no encontrada", exception.getMessage(), "verificacion");
        verify(solicitudRepository).existsById(id);
        verify(solicitudRepository, never()).deleteById(id);
    }

    //tests de la vista inquilino
    @Test
    void testFindByUsuarioIdExistenSolicitudes(){        //Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        SolicitudReserva sol1 = new SolicitudReserva();
        SolicitudReserva sol2 = new SolicitudReserva();
        sol1.setUsuario(usuario);
        sol2.setUsuario(usuario);

        List <SolicitudReserva> solicitudes = List.of(sol1, sol2);

        when(solicitudRepository.findByUsuarioId(id)).thenReturn(solicitudes);

        //Act
        List <SolicitudReserva> resultado = solicitudService.findByUsuarioId(id);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(solicitudes.size(), resultado.size(), "verificacion");
        assertEquals(id, resultado.get(0).getUsuario().getId(), "verificacion");

        verify(solicitudRepository).findByUsuarioId(id);
    }

    @Test
    void testFindByUsuarioIdNoExistenSolicitudes(){        //Arrange
        Long id = 1L;

        when(solicitudRepository.findByUsuarioId(id)).thenReturn(emptyList());

        //Act
        List <SolicitudReserva> resultado = solicitudService.findByUsuarioId(id);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(solicitudRepository).findByUsuarioId(id);
    }

    //test de la vista de propietario
    @Test
    void testObtenerPendientesPropietarioExistenSolicitudes(){        Long idUsuario = 1L;
        String estado = "PENDIENTE";
        SolicitudReserva sol1 = new SolicitudReserva();
        SolicitudReserva sol2 = new SolicitudReserva();
        sol1.setEstado(estado);
        sol2.setEstado(estado);

        List <SolicitudReserva> solicitudes = List.of(sol1, sol2);

        when(solicitudRepository.obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(solicitudes);

        //Act
        List <SolicitudReserva> resultado = solicitudService.obtenerPendientesPropietario(idUsuario);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(solicitudes.size(), resultado.size(), "verificacion");
        assertEquals(estado, resultado.get(0).getEstado(), "verificacion");

        verify(solicitudRepository).obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void testObtenerPendientesPropietarioNoExistenSolicitudes(){        Long idUsuario = 1L;
        String estado = "PENDIENTE";

        when(solicitudRepository.obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(emptyList());
        //Act
        List <SolicitudReserva> resultado = solicitudService.obtenerPendientesPropietario(idUsuario);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");
        verify(solicitudRepository).obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void testContarPendientesPropietarioExistenSolicitudes(){        Long idUsuario = 1L;
        String estado = "PENDIENTE";

        SolicitudReserva sol1 = new SolicitudReserva();
        SolicitudReserva sol2 = new SolicitudReserva();

        when(solicitudRepository.countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(2L);
        //Act
        Long resultado = solicitudService.contarPendientesPropietario(idUsuario);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(2L, resultado, "verificacion");

        verify(solicitudRepository).countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void testContarPendientesPropietarioNoExistenSolicitudes(){        Long idUsuario = 1L;
        String estado = "PENDIENTE";

        when(solicitudRepository.countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(0L);
        //Act
        Long resultado = solicitudService.contarPendientesPropietario(idUsuario);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(0L, resultado, "verificacion");

        verify(solicitudRepository).countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void testCambiarEstadoSolicitudExisteSolicitud(){        Long id = 1L;
        Reserva res = new Reserva();
        res.setActiva(false);

        String nuevoEstado = "ACEPTADA";

        SolicitudReserva sol = new SolicitudReserva();
        sol.setIdSolicitud(id);
        sol.setReserva(res);

        when(solicitudRepository.findById(id)).thenReturn(Optional.of(sol));
        when(solicitudRepository.save(any(SolicitudReserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        SolicitudReserva resultado = solicitudService.cambiarEstadoSolicitud(id, nuevoEstado);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(nuevoEstado, resultado.getEstado(), "verificacion");
        assertTrue(resultado.getReserva().isActiva(), "verificacion");

        verify(solicitudRepository).findById(id);
        verify(solicitudRepository).save(any(SolicitudReserva.class));
    }

    @Test
    void testCambiarEstadoSolicitudExisteSolicitudSinReserva(){        Long id = 1L;
        String nuevoEstado = "RECHAZADA";

        SolicitudReserva sol = new SolicitudReserva();
        sol.setIdSolicitud(id);

        when(solicitudRepository.findById(id)).thenReturn(Optional.of(sol));
        when(solicitudRepository.save(any(SolicitudReserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        SolicitudReserva resultado = solicitudService.cambiarEstadoSolicitud(id, nuevoEstado);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(nuevoEstado, resultado.getEstado(), "verificacion");
        assertNull(resultado.getReserva(), "verificacion");
        verify(solicitudRepository).findById(id);
        verify(solicitudRepository).save(any(SolicitudReserva.class));
    }

    @Test
    void testCambiarEstadoSolicitudNoExisteSolicitud(){        Long id = 1L;
        String nuevoEstado = "ACEPTADA";

        when(solicitudRepository.findById(id)).thenReturn(Optional.empty());

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            solicitudService.cambiarEstadoSolicitud(id, nuevoEstado);
        }, "verificacion");

        assertEquals("Solicitud no encontrada", exception.getMessage(), "verificacion");
        verify(solicitudRepository, never()).save(any(SolicitudReserva.class));
    }
}