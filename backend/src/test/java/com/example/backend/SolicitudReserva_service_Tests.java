package com.example.backend;

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
import com.example.backend.service.SolicitudReservaService;

@ExtendWith(MockitoExtension.class)
public class SolicitudReserva_service_Tests {
    
    @Mock
    private SolicitudReservaRepository solicitudRepository;

    @InjectMocks
    private SolicitudReservaService solicitudService;

    @Test
    void crearSolicitud_GuardadoExitoso_Test(){
        //Arrange
        SolicitudReserva solicitud = new SolicitudReserva();

        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);
        //Act
        SolicitudReserva solicitudGuardada = solicitudService.crearSolicitud(solicitud);
        //Assert
        assertNotNull(solicitudGuardada);
        assertEquals(solicitud, solicitudGuardada);

        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void obtenerTodas_ExistenSolicitudes_Test(){
        //Arrange
        SolicitudReserva sol1 = new SolicitudReserva();
        SolicitudReserva sol2 = new SolicitudReserva();

        List <SolicitudReserva> solicitudes = List.of(sol1, sol2);

        when(solicitudRepository.findAll()).thenReturn(solicitudes);

        //Act
        List<SolicitudReserva> resultado = solicitudService.obtenerTodas();

        //Assert
        assertNotNull(resultado);
        assertEquals(solicitudes.size(), resultado.size());
        assertEquals(solicitudes.get(1), resultado.get(1));

        verify(solicitudRepository).findAll();
    }

    @Test
    void obtenerTodas_NoExistenSolicitudes_Test(){
        //Arrange
        when(solicitudRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <SolicitudReserva> resultado = solicitudService.obtenerTodas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(solicitudRepository).findAll();
    }

    @Test
    void obtenerPorId_ExisteSolicitud_Test(){
        //Arrange
        Long id = 1L;
        SolicitudReserva sol = new SolicitudReserva();
        sol.setIdSolicitud(id);

        when(solicitudRepository.findById(id)).thenReturn(Optional.of(sol));
        //Act
        Optional <SolicitudReserva> resultado = solicitudService.obtenerPorId(id);

        //Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.get().getIdSolicitud());

        verify(solicitudRepository).findById(id);
    }

    @Test
    void obtenerPorId_NoExisteSolicitud_Test(){
        //Arrange
        Long id = 1L;

        when(solicitudRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <SolicitudReserva> resultado = solicitudService.obtenerPorId(id);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(solicitudRepository).findById(id);
    }

    @Test
    void actualizarSolicitud_ExisteSolicitud_Test(){
        //Arrange
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
        assertNotNull(solicitudActualizada);
        assertEquals(solOriginal.getIdSolicitud(), solicitudActualizada.getIdSolicitud());
        assertEquals(solActualizada.getReserva(), solicitudActualizada.getReserva());

        verify(solicitudRepository).findById(id);
        verify(solicitudRepository).save(any(SolicitudReserva.class));
    }

    @Test
    void actualizarSolicitud_NoExisteSolicitud_Test(){
        //Arrange
        Long id = 1L;
        SolicitudReserva nuevaSolicitud = new SolicitudReserva();
        when(solicitudRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            solicitudService.actualizarSolicitud(id, nuevaSolicitud);
        });

        assertEquals("Solicitud no encontrada", exception.getMessage());
        verify(solicitudRepository, never()).save(any(SolicitudReserva.class));

    }

    @Test
    void eliminarSolicitud_ExisteSolicitud_Test(){
        //Arrange
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
    void eliminarSolicitud_NoExisteSolicitud_Test(){
        //Arrange
        Long id = 1L;

        when(solicitudRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            solicitudService.eliminarSolicitud(id);
        });

        assertEquals("Solicitud no encontrada", exception.getMessage());
        verify(solicitudRepository).existsById(id);
        verify(solicitudRepository, never()).deleteById(id);
    }

    //tests de la vista inquilino
    @Test
    void findByUsuarioId_ExistenSolicitudes_Test(){
        //Arrange
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
        assertNotNull(resultado);
        assertEquals(solicitudes.size(), resultado.size());
        assertEquals(id, resultado.get(0).getUsuario().getId());

        verify(solicitudRepository).findByUsuarioId(id);
    }

    @Test
    void findByUsuarioId_NoExistenSolicitudes_Test(){
        //Arrange
        Long id = 1L;

        when(solicitudRepository.findByUsuarioId(id)).thenReturn(emptyList());

        //Act
        List <SolicitudReserva> resultado = solicitudService.findByUsuarioId(id);

        //Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(solicitudRepository).findByUsuarioId(id);
    }

    //test de la vista de propietario
    @Test
    void obtenerPendientesPropietario_ExistenSolicitudes_Test(){
        Long idUsuario = 1L;
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
        assertNotNull(resultado);
        assertEquals(solicitudes.size(), resultado.size());
        assertEquals(estado, resultado.get(0).getEstado());

        verify(solicitudRepository).obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void obtenerPendientesPropietario_NoExistenSolicitudes_Test(){
        Long idUsuario = 1L;
        String estado = "PENDIENTE";

        when(solicitudRepository.obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(emptyList());
        //Act
        List <SolicitudReserva> resultado = solicitudService.obtenerPendientesPropietario(idUsuario);
        //Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(solicitudRepository).obtenerConDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void contarPendientesPropietario_ExistenSolicitudes_Test(){
        Long idUsuario = 1L;
        String estado = "PENDIENTE";

        SolicitudReserva sol1 = new SolicitudReserva();
        SolicitudReserva sol2 = new SolicitudReserva();

        when(solicitudRepository.countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(2L);
        //Act
        Long resultado = solicitudService.contarPendientesPropietario(idUsuario);

        //Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado);

        verify(solicitudRepository).countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void contarPendientesPropietario_NoExistenSolicitudes_Test(){
        Long idUsuario = 1L;
        String estado = "PENDIENTE";

        when(solicitudRepository.countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado)).thenReturn(0L);
        //Act
        Long resultado = solicitudService.contarPendientesPropietario(idUsuario);

        //Assert
        assertNotNull(resultado);
        assertEquals(0L, resultado);

        verify(solicitudRepository).countByDisponibilidadInmueblePropietarioIdAndEstado(idUsuario, estado);
    }

    @Test
    void cambiarEstadoSolicitud_ExisteSolicitud_Test(){
        Long id = 1L;
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
        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getEstado());
        assertTrue(resultado.getReserva().isActiva());

        verify(solicitudRepository).findById(id);
        verify(solicitudRepository).save(any(SolicitudReserva.class));
    }

    @Test
    void cambiarEstadoSolicitud_ExisteSolicitudSinReserva_Test(){
        Long id = 1L;
        String nuevoEstado = "RECHAZADA";

        SolicitudReserva sol = new SolicitudReserva();
        sol.setIdSolicitud(id);

        when(solicitudRepository.findById(id)).thenReturn(Optional.of(sol));
        when(solicitudRepository.save(any(SolicitudReserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        SolicitudReserva resultado = solicitudService.cambiarEstadoSolicitud(id, nuevoEstado);

        //Assert
        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getEstado());
        assertNull(resultado.getReserva());
        verify(solicitudRepository).findById(id);
        verify(solicitudRepository).save(any(SolicitudReserva.class));
    }

    @Test
    void cambiarEstadoSolicitud_noExisteSolicitud_Test(){
        Long id = 1L;
        String nuevoEstado = "ACEPTADA";

        when(solicitudRepository.findById(id)).thenReturn(Optional.empty());

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            solicitudService.cambiarEstadoSolicitud(id, nuevoEstado);
        });

        assertEquals("Solicitud no encontrada", exception.getMessage());
        verify(solicitudRepository, never()).save(any(SolicitudReserva.class));
    }
}
