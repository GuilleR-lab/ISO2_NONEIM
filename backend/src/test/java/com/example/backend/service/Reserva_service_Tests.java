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
import com.example.backend.service.ReservaService;

@ExtendWith(MockitoExtension.class)
public class Reserva_service_Tests {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void crearReserva_GuardadoExitoso_Test(){
        //Arrange
        Reserva reserva = new Reserva();

        when(reservaRepository.save(reserva)).thenReturn(reserva);
        //Act
        Reserva reservaGuardada = reservaService.crearReserva(reserva);

        //Assert
        assertNotNull(reservaGuardada);
        assertEquals(reserva, reservaGuardada);

        verify(reservaRepository).save(reserva);

    }

    @Test
    void obtenerTodas_ExistenReservas_Test(){
        //Arrange
        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();

        List <Reserva> reservas = List.of(reserva1, reserva2);

        when(reservaRepository.findAll()).thenReturn(reservas);
        //Act
        List <Reserva> reservasObtenidas = reservaService.obtenerTodas();

        //Assert
        assertNotNull(reservasObtenidas);
        assertEquals(reservas.size(), reservasObtenidas.size());
        assertEquals(reservas.get(0), reservasObtenidas.get(0));

        verify(reservaRepository).findAll();
    }

    @Test
    void obtenerTodas_NoExistenReservas_Test(){
        //Arrange

        when(reservaRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <Reserva> reservasObtenidas = reservaService.obtenerTodas();

        assertNotNull(reservasObtenidas);
        assertTrue(reservasObtenidas.isEmpty());

        verify(reservaRepository).findAll();
    }

    @Test
    void obtenerPorId_ExisteReserva_Test(){
        //Arrange
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setIdReserva(id);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));
        //Act
        Optional<Reserva> reservaObtenida = reservaService.obtenerPorId(id);
        //Assert
        assertNotNull(reservaObtenida);
        assertEquals(id, reservaObtenida.get().getIdReserva());

        verify(reservaRepository).findById(id);

    }

    @Test
    void obtenerPorId_NoExisteReserva_Test(){
        //Arrange
        Long id = 1L;

        when(reservaRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional<Reserva> reservaObtenida = reservaService.obtenerPorId(id);

        assertNotNull(reservaObtenida);
        assertTrue(reservaObtenida.isEmpty());

        verify(reservaRepository).findById(id);
    }

    @Test
    void obtenerPorInquilino_ExisteReserva_Test(){
        //Arrange

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
        assertNotNull(reservasObtenidas);
        assertEquals(reservas.size(), reservasObtenidas.size());
        assertEquals(reservas.get(0).getInquilino().getId(), reservasObtenidas.get(0).getInquilino().getId());

        verify(reservaRepository).findByInquilinoId(inquilinoId);
    }

    @Test
    void obtenerPorInquilino_NoExisteReserva_Test(){
        //Arrange
        Long inquilinoId = 1L;

        when(reservaRepository.findByInquilinoId(inquilinoId)).thenReturn(emptyList());
        //Act y Assert
        List <Reserva> reservasObtenidas = reservaService.obtenerPorInquilino(inquilinoId);

        assertNotNull(reservasObtenidas);
        assertTrue(reservasObtenidas.isEmpty());

        verify(reservaRepository).findByInquilinoId(inquilinoId);

    }

    @Test
    void obtenerPorInmueble_ExisteReserva_Test(){
        //Arrange
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
        assertNotNull(reservasObtenidas);
        assertEquals(reservas.size(), reservasObtenidas.size());
        assertEquals(reservas.get(0).getInmueble().getIdInmueble(), reservasObtenidas.get(0).getInmueble().getIdInmueble());

        verify(reservaRepository).findByInmuebleId(inmuebleId);
    }

    @Test
    void obtenerPorInmueble_NoExisteReserva_Test(){
        //Arrange
        Long inmuebleId = 1L;

        when(reservaRepository.findByInmuebleId(inmuebleId)).thenReturn(emptyList());
        //Act y Assert
        List <Reserva> reservasObtenidas = reservaService.obtenerPorInmueble(inmuebleId);

        assertNotNull(reservasObtenidas);
        assertTrue(reservasObtenidas.isEmpty());

        verify(reservaRepository).findByInmuebleId(inmuebleId);
    }

    @Test
    void eliminarReserva_ExisteReserva_Test(){
        //Arrange
        Long idReserva = 1L;

        when(reservaRepository.existsById(idReserva)).thenReturn(true);
        //Act 
        reservaService.eliminarReserva(idReserva);

        //Assert
        verify(reservaRepository).existsById(idReserva);
        verify(reservaRepository).deleteById(idReserva);
    }

    @Test
    void eliminarReserva_NoExisteReserva_Test(){
        //Arrange
        Long idReserva = 1L;

        when(reservaRepository.existsById(idReserva)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.eliminarReserva(idReserva);
        });

        assertEquals("Reserva no encontrada", exception.getMessage());

        verify(reservaRepository).existsById(idReserva);
        verify(reservaRepository, never()).deleteById(idReserva);


    }
    
}
