package com.example.backend.service;

import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.model.Inmueble;
import com.example.backend.model.Reserva;
import com.example.backend.model.Usuario;
import com.example.backend.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTests {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void testCrearReservaGuardadoExitoso(){        //Arrange
        Reserva reserva = new Reserva();

        when(reservaRepository.save(reserva)).thenReturn(reserva);
        //Act
        Reserva reservaGuardada = reservaService.crearReserva(reserva);

        //Assert
        assertNotNull(reservaGuardada, "verificacion");
        assertEquals(reserva, reservaGuardada, "verificacion");

        verify(reservaRepository).save(reserva);

    }

    @Test
    void testObtenerTodasExistenReservas(){        //Arrange
        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();

        List <Reserva> reservas = List.of(reserva1, reserva2);

        when(reservaRepository.findAll()).thenReturn(reservas);
        //Act
        List <Reserva> reservasObtenidas = reservaService.obtenerTodas();

        //Assert
        assertNotNull(reservasObtenidas, "verificacion");
        assertEquals(reservas.size(), reservasObtenidas.size(), "verificacion");
        assertEquals(reservas.get(0), reservasObtenidas.get(0), "verificacion");

        verify(reservaRepository).findAll();
    }

    @Test
    void testObtenerTodasNoExistenReservas(){        //Arrange

        when(reservaRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <Reserva> reservasObtenidas = reservaService.obtenerTodas();

        assertNotNull(reservasObtenidas, "verificacion");
        assertTrue(reservasObtenidas.isEmpty(), "verificacion");

        verify(reservaRepository).findAll();
    }

    @Test
    void testObtenerPorIdExisteReserva(){        //Arrange
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setIdReserva(id);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));
        //Act
        Optional<Reserva> reservaObtenida = reservaService.obtenerPorId(id);
        //Assert
        assertNotNull(reservaObtenida, "verificacion");
        assertEquals(id, reservaObtenida.get().getIdReserva(), "verificacion");

        verify(reservaRepository).findById(id);

    }

    @Test
    void testObtenerPorIdNoExisteReserva(){        //Arrange
        Long id = 1L;

        when(reservaRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional<Reserva> reservaObtenida = reservaService.obtenerPorId(id);

        assertNotNull(reservaObtenida, "verificacion");
        assertTrue(reservaObtenida.isEmpty(), "verificacion");

        verify(reservaRepository).findById(id);
    }

    @Test
    void testObtenerPorInquilinoExisteReserva(){        //Arrange

        Long inquilinoId = 1L;

        Usuario inquilino = new Usuario();
        inquilino.setId(inquilinoId);
        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();
        reserva1.setInquilino(inquilino);
        reserva2.setInquilino(inquilino);

        List <Reserva> reservas = List.of(reserva1, reserva2);

        when(reservaRepository.findByInquilinoId(inquilinoId)).thenReturn(reservas);

        //Act
        List <Reserva> reservasObtenidas = reservaService.obtenerPorInquilino(inquilinoId);

        //Assert
        assertNotNull(reservasObtenidas, "verificacion");
        assertEquals(reservas.size(), reservasObtenidas.size(), "verificacion");
        assertEquals(reservas.get(0).getInquilino().getId(), reservasObtenidas.get(0).getInquilino().getId(), "verificacion");

        verify(reservaRepository).findByInquilinoId(inquilinoId);
    }

    @Test
    void testObtenerPorInquilinoNoExisteReserva(){        //Arrange
        Long inquilinoId = 1L;

        when(reservaRepository.findByInquilinoId(inquilinoId)).thenReturn(emptyList());
        //Act y Assert
        List <Reserva> reservasObtenidas = reservaService.obtenerPorInquilino(inquilinoId);

        assertNotNull(reservasObtenidas, "verificacion");
        assertTrue(reservasObtenidas.isEmpty(), "verificacion");

        verify(reservaRepository).findByInquilinoId(inquilinoId);

    }

    @Test
    void testObtenerPorInmuebleExisteReserva(){        //Arrange
        Long inmuebleId = 1L;

        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(inmuebleId);
        reserva1.setInmueble(inmueble);
        reserva2.setInmueble(inmueble);

        List <Reserva> reservas = List.of(reserva1, reserva2);

        when(reservaRepository.findByInmuebleId(inmuebleId)).thenReturn(reservas);
        //Act
        List <Reserva> reservasObtenidas = reservaService.obtenerPorInmueble(inmuebleId);

        //Assert
        assertNotNull(reservasObtenidas, "verificacion");
        assertEquals(reservas.size(), reservasObtenidas.size(), "verificacion");
        assertEquals(reservas.get(0).getInmueble().getIdInmueble(), reservasObtenidas.get(0).getInmueble().getIdInmueble(), "verificacion");

        verify(reservaRepository).findByInmuebleId(inmuebleId);
    }

    @Test
    void testObtenerPorInmuebleNoExisteReserva(){        //Arrange
        Long inmuebleId = 1L;

        when(reservaRepository.findByInmuebleId(inmuebleId)).thenReturn(emptyList());
        //Act y Assert
        List <Reserva> reservasObtenidas = reservaService.obtenerPorInmueble(inmuebleId);

        assertNotNull(reservasObtenidas, "verificacion");
        assertTrue(reservasObtenidas.isEmpty(), "verificacion");

        verify(reservaRepository).findByInmuebleId(inmuebleId);
    }

    @Test
    void testEliminarReservaExisteReserva(){        //Arrange
        Long idReserva = 1L;

        when(reservaRepository.existsById(idReserva)).thenReturn(true);
        //Act 
        reservaService.eliminarReserva(idReserva);

        //Assert
        verify(reservaRepository).existsById(idReserva);
        verify(reservaRepository).deleteById(idReserva);
    }

    @Test
    void testEliminarReservaNoExisteReserva(){        //Arrange
        Long idReserva = 1L;

        when(reservaRepository.existsById(idReserva)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.eliminarReserva(idReserva);
        }, "verificacion");

        assertEquals("Reserva no encontrada", exception.getMessage(), "verificacion");

        verify(reservaRepository).existsById(idReserva);
        verify(reservaRepository, never()).deleteById(idReserva);


    }
    
}