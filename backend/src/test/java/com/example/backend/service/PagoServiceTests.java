package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.example.backend.model.Pago;
import com.example.backend.model.Reserva;
import com.example.backend.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTests {
    
    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void testCrearPagoGuardadoExitoso(){        //Arrange
        Pago pago = new Pago();
        pago.setReferencia(1L);

        when(pagoRepository.save(pago)).thenReturn(pago);
        //Act
        Pago pagoGuardado = pagoService.crearPago(pago);

        //Assert
        assertNotNull(pagoGuardado, "verificacion");
        assertEquals(pago, pagoGuardado, "verificacion");

        verify(pagoRepository).save(pago);
    }

    @Test
    void testObtenerTodosExistenPagos(){        //Arrange
        Pago pago1 = new Pago();
        Pago pago2 = new Pago();
        pago1.setReferencia(1L);
        pago2.setReferencia(2L);
        List<Pago> pagos = List.of(pago1, pago2);

        when(pagoRepository.findAll()).thenReturn(pagos);
        //Act
        List<Pago> pagosObtenidos = pagoService.obtenerTodos();

        //Assert
        assertNotNull(pagosObtenidos, "verificacion");
        assertEquals(pagos.size(), pagosObtenidos.size(), "verificacion");
        assertEquals(pago1.getReferencia(), pagosObtenidos.get(0).getReferencia(), "verificacion");

        verify(pagoRepository).findAll();

    }

    @Test
    void testObtenerPorReferenciaPagoExiste(){        //Arrange
        Pago pago = new Pago();
        Long ref_pago = 1L;
        pago.setReferencia(ref_pago);

        when(pagoRepository.findById(ref_pago)).thenReturn(Optional.of(pago));
        //Act
        Optional<Pago> pagoObtenido = pagoService.obtenerPorReferencia(ref_pago);

        //Assert
        assertNotNull(pagoObtenido, "verificacion");
        assertEquals(ref_pago, pagoObtenido.get().getReferencia(), "verificacion");

        verify(pagoRepository).findById(ref_pago);

    }

    @Test
    void testObtenerPorReferenciaNoExistePago(){        //Arrange
        Long ref_pago = 1L;

        when(pagoRepository.findById(ref_pago)).thenReturn(Optional.empty());
        //Act y Assert
        Optional<Pago> pagoObtenido = pagoService.obtenerPorReferencia(ref_pago);

        //Assert
        assertNotNull(pagoObtenido, "verificacion");
        assertTrue(pagoObtenido.isEmpty(), "verificacion");

        verify(pagoRepository).findById(ref_pago);

    }

    @Test
    void testActualizarPagoPagoExiste(){        //Arrange
        Long ref_pago = 1L;
        Reserva reserva = new Reserva();
        Pago pagoOriginal = new Pago("TARJETA_CREDITO", 100.0, reserva);
        pagoOriginal.setReferencia(ref_pago);

        Pago pagoNuevo = new Pago("TARJETA_CREDITO", 200.0, reserva);
        pagoNuevo.setReferencia(ref_pago);

        when(pagoRepository.findById(ref_pago)).thenReturn(Optional.of(pagoOriginal));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        Pago resultado = pagoService.actualizarPago(ref_pago, pagoNuevo);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(pagoNuevo.getImporte(), resultado.getImporte(), "verificacion");
        assertEquals(pagoOriginal.getReserva(), resultado.getReserva(), "verificacion");

        verify(pagoRepository).findById(ref_pago);
        verify(pagoRepository).save(any(Pago.class));

    }

    @Test
    void testActualizarPagoNoExistePago(){        //Arrange
        Long ref_pago = 1L;
        Pago pagoNuevo = new Pago("TARJETA_CREDITO", 200.0, new Reserva());
        

        when(pagoRepository.findById(ref_pago)).thenReturn(Optional.empty());

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            pagoService.actualizarPago(ref_pago, pagoNuevo);
        }, "verificacion");

        assertEquals("Pago no encontrado", exception.getMessage(), "verificacion");
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void testEliminarPagoExistePago(){        //Arrange
        Long ref_pago = 1L;

        when(pagoRepository.existsById(ref_pago)).thenReturn(true);
        //Act
        pagoService.eliminarPago(ref_pago);

        //Assert
        verify(pagoRepository).existsById(ref_pago);
        verify(pagoRepository).deleteById(ref_pago);

        verifyNoMoreInteractions(pagoRepository);
    }

    @Test
    void testEliminarPagoNoExistePago(){        //Arrange
        Long ref_pago = 1L;

        when(pagoRepository.existsById(ref_pago)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            pagoService.eliminarPago(ref_pago);
        }, "verificacion");

        assertEquals("No se puede eliminar: Pago no encontrado", exception.getMessage(), "verificacion");
        verify(pagoRepository).existsById(ref_pago);
        verify(pagoRepository, never()).deleteById(ref_pago);
    }
}