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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.model.Inmueble;
import com.example.backend.model.Usuario;
import com.example.backend.repository.InmuebleRepository;
import com.example.backend.service.InmuebleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class Inmueble_Service_Tests {

    @Mock
    private InmuebleRepository inmuebleRepository;

    @InjectMocks
    private InmuebleServiceImpl inmuebleService;

    @Test
    void crearInmueble_GuaradadoExitoso_Test(){
        //Arrange
        Usuario prop = new Usuario();
        prop.setId(1L);
        Inmueble inmuebleGuardado = new Inmueble("Calle Falsa 123", "Madrid",
            100.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripción", prop
        );
        inmuebleGuardado.setIdInmueble(2L);

        when(inmuebleRepository.save(any(Inmueble.class))).thenReturn(inmuebleGuardado);
        
        //Act
        Inmueble resultado = inmuebleService.crearInmueble(inmuebleGuardado);

        //Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado.getIdInmueble());
        assertEquals("Calle Falsa 123", resultado.getDireccion());

        verify(inmuebleRepository).save(inmuebleGuardado);
    }

    @Test
    void obtenerTodos_ExistenInmuebles_Test(){
        //Arrange
        Usuario prop = new Usuario();
        prop.setId(1L);
        Inmueble inmueble = new Inmueble("Calle Falsa 123", "Madrid",
            100.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripcion", prop
        );
        inmueble.setIdInmueble(2L);

        when(inmuebleRepository.findAll()).thenReturn(List.of(inmueble));

        //Act
        List <Inmueble> resultado = inmuebleService.obtenerTodos();
        //Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado.get(0).getIdInmueble());

        verify(inmuebleRepository).findAll();
    }

    @Test
    void obtenerTodos_NoExistenInmuebles_Test(){
        //Arrange
        when(inmuebleRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <Inmueble> resultado = inmuebleService.obtenerTodos();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inmuebleRepository).findAll();
    }

    @Test
    void obtenerPorId_ExisteInmueble_Test(){
        //Arrange
        Usuario prop = new Usuario();
        prop.setId(1L);
        Inmueble inmueble = new Inmueble("Calle Falsa 123", "Madrid",
            100.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripcion", prop
        );
        inmueble.setIdInmueble(2L);

        when(inmuebleRepository.findById(2L)).thenReturn(Optional.of(inmueble));
        //Act
        Optional <Inmueble> resultado = inmuebleService.obtenerPorId(2L);

        //Assert
        assertTrue(resultado.isPresent());
        assertEquals(2L, resultado.get().getIdInmueble());
        assertEquals(100.0, resultado.get().getPrecioNoche());

        verify(inmuebleRepository).findById(2L);
    }

    @Test
    void obtenerPorId_NoExisteInmueble_Test(){
        //Arrange
        Long id = 1L;
        when(inmuebleRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <Inmueble> resultado = inmuebleService.obtenerPorId(id);
       
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inmuebleRepository).findById(id);
    }

    @Test
    void actualizarInmueble_ExisteInmueble_Test(){
        //Arrange
        Usuario prop = new Usuario();
        prop.setId(1L);

        Inmueble inmuebleOriginal = new Inmueble("Calle Falsa 123", "Madrid",
            100.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripcion", prop
        );

        Inmueble inmuebleNuevo = new Inmueble("Calle Nueva 456", "Madrid",
            100.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripcion", prop
        );
        inmuebleOriginal.setIdInmueble(2L);
        inmuebleNuevo.setIdInmueble(2L);

        when(inmuebleRepository.findById(2L)).thenReturn(Optional.of(inmuebleOriginal));
        when(inmuebleRepository.save(any(Inmueble.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        Inmueble resultado = inmuebleService.actualizarInmueble(2L, inmuebleNuevo);

        //Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado.getIdInmueble());
        assertEquals("Calle Nueva 456", resultado.getDireccion());
        assertEquals(inmuebleNuevo.getPrecioNoche(), resultado.getPrecioNoche());

        verify(inmuebleRepository).findById(2L);
        verify(inmuebleRepository).save(any(Inmueble.class));
    }

    @Test
    void actualizarInmueble_NoExisteInmueble_Test(){
        //Arrange
        Long id = 1L;
        Inmueble inmuebleNuevo = new Inmueble();
        inmuebleNuevo.setIdInmueble(id);

        when(inmuebleRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.actualizarInmueble(id, inmuebleNuevo);
        });

        assertEquals("Inmueble no encontrado", exception.getMessage());

        verify(inmuebleRepository, never()).save(any(Inmueble.class));
    }

    @Test
    void eliminarInmueble_ExisteInmueble_Test(){
        //Arrange
        Long id = 1L;

        when(inmuebleRepository.existsById(id)).thenReturn(true);
        //Act
        inmuebleService.eliminarInmueble(id);

        //Assert
        verify(inmuebleRepository).existsById(id);
        verify(inmuebleRepository).deleteById(id);

    }

    @Test
    void eliminarInmueble_NoExisteInmueble_Test(){
        //Arrange
        Long id = 1L;

        when(inmuebleRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.eliminarInmueble(id);
        });

        assertEquals("Inmueble no encontrado", exception.getMessage());

        verify(inmuebleRepository, never()).deleteById(id);
    }

    @Test
    void buscarConFiltros_ExistenInmueblesTodosCampos_Test(){
        //Arrange
        String ciudad = "Madrid";
        Inmueble.Tipo tipo = Inmueble.Tipo.APARTAMENTO;
        boolean soloDirecta = true;
        LocalDate fechaInicio = LocalDate.of(2026, 5, 10);
        LocalDate fechaFin = LocalDate.of(2026, 5, 20);

        List <Inmueble> listaEsperada = List.of(new Inmueble(), new Inmueble());

        when(inmuebleRepository.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin)).thenReturn(listaEsperada);
        //Act
        List <Inmueble> resultado = inmuebleService.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);

        //Assert
        assertNotNull(resultado);
        assertEquals(listaEsperada.size(), resultado.size());

        verify(inmuebleRepository).buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);

    }

    @Test
    void buscarConFiltros_ExistenInmueblesSoloCiudad_Test(){
        //Arrange
        String ciudad = "Salamanca";

        List <Inmueble> listaEsperada = List.of(new Inmueble(), new Inmueble());
        when(inmuebleRepository.buscarConFiltros(ciudad, null, false, null, null)).thenReturn(listaEsperada);
        //Act
        List <Inmueble> resultado = inmuebleService.buscarConFiltros(ciudad, null, false, null, null);

        //Assert
        assertNotNull(resultado);
        assertEquals(listaEsperada.size(), resultado.size());

        verify(inmuebleRepository).buscarConFiltros(ciudad, null, false, null, null);
    }

    @Test
    void buscarConFiltros_NoExistenInmuebles_Test(){
        //Arrange
        String ciudad = "Sevilla";
        Inmueble.Tipo tipo = Inmueble.Tipo.VIVIENDA_COMPLETA;
        boolean soloDirecta = false;
        LocalDate fechaInicio = LocalDate.of(2026, 5, 10);
        LocalDate fechaFin = LocalDate.of(2026, 5, 20);

        List <Inmueble> listaEsperada = emptyList();
        when(inmuebleRepository.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin)).thenReturn(listaEsperada);
        //Act y Assert
        List <Inmueble> resultado = inmuebleService.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);

        //Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inmuebleRepository).buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);
    }

    @Test
    void buscarConFiltros_SinCoincidencias_Test(){
        //Arrange
        String ciudad = "Sevilla";
        Inmueble.Tipo tipo = Inmueble.Tipo.VIVIENDA_COMPLETA;
        boolean soloDirecta = false;
        LocalDate fechaInicio = LocalDate.of(2026, 5, 10);
        LocalDate fechaFin = LocalDate.of(2026, 5, 20);
        when(inmuebleRepository.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin)).thenReturn(emptyList());
        //Act
        List <Inmueble> resultado = inmuebleService.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);

        //Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inmuebleRepository).buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);
    }

    @Test
    void obtenerPorPropietario_ExisteInmueble_Test(){
        //Arrange
        Usuario prop1 = new Usuario(); 
        prop1.setId(1L); 

        Inmueble inmueble1 = new Inmueble("Calle Falsa 123", "Madrid",
            150.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Desceipcion", prop1
        );

        Inmueble inmueble2 = new Inmueble("Calle Nueva 456", "Barcelona",
            206.0, Inmueble.Tipo.APARTAMENTO, "Descripcion", prop1
        );

        when(inmuebleRepository.findByPropietarioId(1L)).thenReturn(List.of(inmueble1, inmueble2));
        //Act
        List <Inmueble> resultado = inmuebleService.obtenerPorPropietario(1L);

        //Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains(inmueble1));
        assertTrue(resultado.contains(inmueble2));
        assertEquals(1L, resultado.get(0).getPropietario().getId());
        assertEquals(1L, resultado.get(1).getPropietario().getId());


        verify(inmuebleRepository).findByPropietarioId(1L);
        verifyNoMoreInteractions(inmuebleRepository);
    }

    @Test
    void obtenerPorPropietario_NoExisteInmueble_Test(){
        //Arrange
        Long id = 1L;

        when(inmuebleRepository.findByPropietarioId(id)).thenReturn(emptyList());

        //Act y Assert
        List<Inmueble> resultado = inmuebleService.obtenerPorPropietario(id);

        //Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inmuebleRepository).findByPropietarioId(id);
        verifyNoMoreInteractions(inmuebleRepository);
    }

    
}
