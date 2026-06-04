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

@ExtendWith(MockitoExtension.class)
public class InmuebleServiceTests {

    @Mock
    private InmuebleRepository inmuebleRepository;

    @InjectMocks
    private InmuebleServiceImpl inmuebleService;

    @Test
    void testCrearInmueble_GuaradadoExitoso(){
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
        assertNotNull(resultado, "verificacion");
        assertEquals(2L, resultado.getIdInmueble(), "verificacion");
        assertEquals("Calle Falsa 123", resultado.getDireccion(), "verificacion");

        verify(inmuebleRepository).save(inmuebleGuardado);
    }

    @Test
    void testObtenerTodos_ExistenInmuebles(){
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
        assertNotNull(resultado, "verificacion");
        assertEquals(2L, resultado.get(0).getIdInmueble(), "verificacion");

        verify(inmuebleRepository).findAll();
    }

    @Test
    void testObtenerTodos_NoExistenInmuebles(){
        //Arrange
        when(inmuebleRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <Inmueble> resultado = inmuebleService.obtenerTodos();
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(inmuebleRepository).findAll();
    }

    @Test
    void testObtenerPorId_ExisteInmueble(){
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
        assertTrue(resultado.isPresent(), "verificacion");
        assertEquals(2L, resultado.get().getIdInmueble(), "verificacion");
        assertEquals(100.0, resultado.get().getPrecioNoche(), "verificacion");

        verify(inmuebleRepository).findById(2L);
    }

    @Test
    void testObtenerPorId_NoExisteInmueble(){
        //Arrange
        Long id = 1L;
        when(inmuebleRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <Inmueble> resultado = inmuebleService.obtenerPorId(id);
       
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(inmuebleRepository).findById(id);
    }

    @Test
    void testActualizarInmueble_ExisteInmueble(){
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
        assertNotNull(resultado, "verificacion");
        assertEquals(2L, resultado.getIdInmueble(), "verificacion");
        assertEquals("Calle Nueva 456", resultado.getDireccion(), "verificacion");
        assertEquals(inmuebleNuevo.getPrecioNoche(), resultado.getPrecioNoche(), "verificacion");

        verify(inmuebleRepository).findById(2L);
        verify(inmuebleRepository).save(any(Inmueble.class));
    }

    @Test
    void testActualizarInmueble_NoExisteInmueble(){
        //Arrange
        Long id = 1L;
        Inmueble inmuebleNuevo = new Inmueble();
        inmuebleNuevo.setIdInmueble(id);

        when(inmuebleRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.actualizarInmueble(id, inmuebleNuevo);
        });

        assertEquals("Inmueble no encontrado", exception.getMessage(), "verificacion");

        verify(inmuebleRepository, never()).save(any(Inmueble.class));
    }

    @Test
    void testEliminarInmueble_ExisteInmueble(){
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
    void testEliminarInmueble_NoExisteInmueble(){
        //Arrange
        Long id = 1L;

        when(inmuebleRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.eliminarInmueble(id);
        });

        assertEquals("Inmueble no encontrado", exception.getMessage(), "verificacion");

        verify(inmuebleRepository, never()).deleteById(id);
    }

    @Test
    void testBuscarConFiltros_ExistenInmueblesTodosCampos(){
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
        assertNotNull(resultado, "verificacion");
        assertEquals(listaEsperada.size(), resultado.size(), "verificacion");

        verify(inmuebleRepository).buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);

    }

    @Test
    void testBuscarConFiltros_ExistenInmueblesSoloCiudad(){
        //Arrange
        String ciudad = "Salamanca";

        List <Inmueble> listaEsperada = List.of(new Inmueble(), new Inmueble());
        when(inmuebleRepository.buscarConFiltros(ciudad, null, false, null, null)).thenReturn(listaEsperada);
        //Act
        List <Inmueble> resultado = inmuebleService.buscarConFiltros(ciudad, null, false, null, null);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(listaEsperada.size(), resultado.size(), "verificacion");

        verify(inmuebleRepository).buscarConFiltros(ciudad, null, false, null, null);
    }

    @Test
    void testBuscarConFiltros_NoExistenInmuebles(){
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
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(inmuebleRepository).buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);
    }

    @Test
    void testBuscarConFiltros_SinCoincidencias(){
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
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(inmuebleRepository).buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);
    }

    @Test
    void testObtenerPorPropietario_ExisteInmueble(){
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
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.contains(inmueble1), "verificacion");
        assertTrue(resultado.contains(inmueble2), "verificacion");
        assertEquals(1L, resultado.get(0).getPropietario().getId(), "verificacion");
        assertEquals(1L, resultado.get(1).getPropietario().getId(), "verificacion");


        verify(inmuebleRepository).findByPropietarioId(1L);
        verifyNoMoreInteractions(inmuebleRepository);
    }

    @Test
    void testObtenerPorPropietario_NoExisteInmueble(){
        //Arrange
        Long id = 1L;

        when(inmuebleRepository.findByPropietarioId(id)).thenReturn(emptyList());

        //Act y Assert
        List<Inmueble> resultado = inmuebleService.obtenerPorPropietario(id);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertTrue(resultado.isEmpty(), "verificacion");

        verify(inmuebleRepository).findByPropietarioId(id);
        verifyNoMoreInteractions(inmuebleRepository);
    }

    
}