package com.example.backend.service;

import java.time.LocalDate;
import static java.util.Collections.emptyList;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.repository.DisponibilidadRepository;
import com.example.backend.service.DisponibilidadService;

@ExtendWith(MockitoExtension.class)
public class Disponibilidad_service_Tests {

    @Mock
    private DisponibilidadRepository disponibilidadRepository;

    @InjectMocks
    private DisponibilidadService disponibilidadService;

    
    @Test
    void crearDisponibilidad_GuardadoExitoso_Test(){
        // Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);
        
        Disponibilidad disponibilidad = new Disponibilidad(LocalDate.of(2026, 5, 10), 
            LocalDate.of(2026, 5, 20), 150.0, true, inmueble
        );
        
        Disponibilidad disponibilidadGuardada = new Disponibilidad(LocalDate.of(2026, 5, 10), 
            LocalDate.of(2026, 5, 20), 150.0, true, inmueble
        );
        disponibilidadGuardada.setIdDisponibilidad(1L);
        
        when(disponibilidadRepository.save(disponibilidad)).thenReturn(disponibilidadGuardada);
        
        // Act
        Disponibilidad resultado = disponibilidadService.crearDisponibilidad(disponibilidad);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdDisponibilidad());
        assertEquals(150.0, resultado.getPrecio());
        assertEquals(inmueble, resultado.getInmueble());
        
        verify(disponibilidadRepository, times(1)).save(disponibilidad);
    }

    @Test
    void obtenerListaDisponibilidades_ListaVacia_Test(){
        // Arrange
        when(disponibilidadRepository.findAll()).thenReturn(emptyList());
        
        // Act
        List<Disponibilidad> resultado = disponibilidadService.obtenerTodas();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(disponibilidadRepository, times(1)).findAll();
    }

    @Test 
    void obtenerListaDisponibilidades_ListaConElementos_Test(){
        // Arrange
        
        Inmueble inmueble1 = new Inmueble();
        Inmueble inmueble2 = new Inmueble();
        inmueble1.setIdInmueble(1L);
        inmueble2.setIdInmueble(2L);
        
        Disponibilidad disponibilidad1 = new Disponibilidad(LocalDate.of(2026, 5, 10), 
            LocalDate.of(2026, 5, 20), 150.0, true, inmueble1
        );

        Disponibilidad disponibilidad2 = new Disponibilidad(LocalDate.of(2026, 6, 1), 
            LocalDate.of(2026, 6, 10), 200.0, false, inmueble2
        );

        disponibilidad1.setIdDisponibilidad(1L);
        disponibilidad2.setIdDisponibilidad(2L);
        
        java.util.List<Disponibilidad> disponibilidades = java.util.Arrays.asList(disponibilidad1, disponibilidad2);
        
        when(disponibilidadRepository.findAll()).thenReturn(disponibilidades);
        
        // Act
        java.util.List<Disponibilidad> resultado = disponibilidadService.obtenerTodas();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        verify(disponibilidadRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_DisponibilidadExistente_Test(){
        //Arrange
        Long id = 1L;
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);

        Disponibilidad dispo = new Disponibilidad( LocalDate.of(2026, 5, 10),
            LocalDate.of(2026, 5, 20), 150.0, true, inmueble);

        dispo.setIdDisponibilidad(id);

        when(disponibilidadRepository.findById(id)).thenReturn(Optional.of(dispo));

        //Act
        Optional<Disponibilidad> resultado = disponibilidadService.obtenerPorId(id);

        //Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdDisponibilidad());
        assertEquals(150.0, resultado.get().getPrecio());

        verify(disponibilidadRepository).findById(id);
    }

    @Test
    void obtenerPorId_DisponibilidadNoExiste_Test(){
        //Arrange
        Long id = 1L;

        when(disponibilidadRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        Optional<Disponibilidad> resultado = disponibilidadService.obtenerPorId(id);

        //Assert
        assertTrue(resultado.isEmpty());
    }

    @Test 
    void actualizarDisponibilidad_ExisteDisponibilidad_Test(){
        //Arrange
        Long id = 1L;

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);

        Disponibilidad dispoOriginal = new Disponibilidad(LocalDate.of(2026, 5, 10),
            LocalDate.of(2026, 5, 20), 150.0, true, inmueble);
        dispoOriginal.setIdDisponibilidad(id);

        Disponibilidad dispoNueva = new Disponibilidad(LocalDate.of(2026, 5, 10),
            LocalDate.of(2026, 5, 20), 200.0, true, inmueble);
        dispoNueva.setIdDisponibilidad(id);

        when(disponibilidadRepository.findById(id)).thenReturn(Optional.of(dispoOriginal));
        when(disponibilidadRepository.save(any(Disponibilidad.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when(disponibilidadRepository.save(any(Disponibilidad.class))).thenReturn(dispoNueva);

        //Act
        Disponibilidad resultado = disponibilidadService.actualizarDisponibilidad(id, dispoNueva);

        //Assert
        assertNotNull(resultado);
        assertEquals(200.0, resultado.getPrecio());
        assertEquals(dispoNueva.getFechaInicio(), resultado.getFechaInicio());

        verify(disponibilidadRepository).findById(id);
        verify(disponibilidadRepository).save(any(Disponibilidad.class));

    }

    @Test
    void actualizarDisponibilidad_NoExisteDisponibilidad_Test(){

        //Arrange
        long id = 1L;

        Disponibilidad dispoNueva = new Disponibilidad(LocalDate.of(2026, 5, 10),
            LocalDate.of(2026, 5, 20), 200.0, true, null);

        when(disponibilidadRepository.findById(id)).thenReturn(Optional.empty());

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            disponibilidadService.actualizarDisponibilidad(id, dispoNueva);
        });

        assertEquals("Disponibilidad no encontrada", exception.getMessage());

        verify(disponibilidadRepository, never()).save(any(Disponibilidad.class));
        
    }

    @Test
    void eliminarDisponibilidad_ExisteDisponibilidad_Test(){
        //Arrange
        Long id = 1L;

        when(disponibilidadRepository.existsById(id)).thenReturn(true);

        //Act
        disponibilidadService.eliminarDisponibilidad(id);

        //Assert
        
        verify(disponibilidadRepository).existsById(id);
        verify(disponibilidadRepository).deleteById(id);

    }

    @Test
    void eliminarDisponibilidad_NoExisteDisponibilidad_Test(){
        //Arrange
        Long id = 1L;

        when(disponibilidadRepository.existsById(id)).thenReturn(false);

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            disponibilidadService.eliminarDisponibilidad(id);
        });

        assertEquals("Disponibilidad no encontrada", exception.getMessage());

        verify(disponibilidadRepository, never()).deleteById(id);
    }

    
}
