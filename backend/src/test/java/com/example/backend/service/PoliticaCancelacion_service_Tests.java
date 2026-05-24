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
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.model.PoliticaCancelacion;
import com.example.backend.repository.PoliticaCancelacionRepository;
import com.example.backend.service.PoliticaCancelacionService;

@ExtendWith(MockitoExtension.class)
public class PoliticaCancelacion_service_Tests {
    
    @Mock
    private PoliticaCancelacionRepository politicaCancelacionRepository;

    @InjectMocks
    private PoliticaCancelacionService politicaCancelacionService;

    @Test
    void crearPolitica_GuardadoExitoso_Test(){
        //Arrange
        PoliticaCancelacion politica = new PoliticaCancelacion();

        when(politicaCancelacionRepository.save(politica)).thenReturn(politica);
        //Act
        PoliticaCancelacion politicaGuardada = politicaCancelacionService.crearPolitica(politica);
        //Assert
        assertNotNull(politicaGuardada);
        assertEquals(politica, politicaGuardada);
    }

    @Test
    void obtenerTodas_ExistenPoliticas_Test(){
        //Arrange
        PoliticaCancelacion politica1 = new PoliticaCancelacion();
        PoliticaCancelacion politica2 = new PoliticaCancelacion();

        List <PoliticaCancelacion> politicas = List.of(politica1, politica2);

        when(politicaCancelacionRepository.findAll()).thenReturn(politicas);
        //Act 
        List <PoliticaCancelacion> politicasObtenidas = politicaCancelacionService.obtenerTodas();
        //Assert
        assertNotNull(politicasObtenidas);
        assertEquals(politicas.size(), politicasObtenidas.size());
        assertEquals(politicas.get(0), politicasObtenidas.get(0));

        verify(politicaCancelacionRepository).findAll();
    }

    @Test
    void obtenerTodas_NoExistenPoliticas_Test(){
        //Arrange
        when(politicaCancelacionRepository.findAll()).thenReturn(List.of());
        //Act
        List <PoliticaCancelacion> politicasObtenidas = politicaCancelacionService.obtenerTodas();
        //Assert
        assertNotNull(politicasObtenidas);
        assertTrue(politicasObtenidas.isEmpty());

        verify(politicaCancelacionRepository).findAll();
    }

    @Test
    void obtenerPorId_ExistePolitica_Test(){
        //Arrange
        Long id = 1L;
        PoliticaCancelacion politica = new PoliticaCancelacion("Descripcion", 10.0);
        politica.setIdPolitica(id);

        when(politicaCancelacionRepository.findById(id)).thenReturn(java.util.Optional.of(politica));
        //Act
        Optional <PoliticaCancelacion> politicaObtenida = politicaCancelacionService.obtenerPorId(id);

        //Assert
        assertNotNull(politicaObtenida);
        assertEquals(politica.getDescripcion(), politicaObtenida.get().getDescripcion());

        verify(politicaCancelacionRepository).findById(id);
    }

    @Test
    void obtenerPorId_NoExistePolitica_Test(){
        //Arrange
        Long id = 1L;

        when(politicaCancelacionRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <PoliticaCancelacion> politicaObtenida = politicaCancelacionService.obtenerPorId(id);

        //Assert
        assertNotNull(politicaObtenida);
        assertTrue(politicaObtenida.isEmpty());

        verify(politicaCancelacionRepository).findById(id);
    }

    @Test
    void actualizarPolitica_ExistePolitica_Test(){
        //Arrange
        Long id = 1L;
        String descripcion = "Descripcion";
        double penalizacion = 10.0;
        double nuevaPenalizacion = 20.0;
        PoliticaCancelacion politicaOriginal = new PoliticaCancelacion(descripcion, penalizacion);

        when(politicaCancelacionRepository.findById(id)).thenReturn(Optional.of(politicaOriginal));
        when(politicaCancelacionRepository.save(any(PoliticaCancelacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        PoliticaCancelacion politicaActualizada = politicaCancelacionService.actualizarPolitica(id, new PoliticaCancelacion(descripcion, nuevaPenalizacion));

        //Assert
        assertNotNull(politicaActualizada);
        assertEquals(nuevaPenalizacion, politicaActualizada.getPenalizacion());
        assertEquals(politicaOriginal.getDescripcion(), politicaActualizada.getDescripcion());

        verify(politicaCancelacionRepository).findById(id);
        verify(politicaCancelacionRepository).save(any(PoliticaCancelacion.class));

    }

    @Test
    void actualizarPolitica_NoExistePolitica_Test(){
        //Arrange
        Long id = 1L;

        PoliticaCancelacion nuevaPolitica = new PoliticaCancelacion("Descripcion", 10.0);

        when(politicaCancelacionRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            politicaCancelacionService.actualizarPolitica(id, nuevaPolitica);
        });

        assertEquals("Política no encontrada", exception.getMessage());

        verify(politicaCancelacionRepository, never()).save(any(PoliticaCancelacion.class));
    }

    @Test
    void eliminarPolitica_ExistePolitica_Test(){
        //Arrange
        Long id = 1L;
        
        when(politicaCancelacionRepository.existsById(id)).thenReturn(true);
        //Act
        politicaCancelacionService.eliminarPolitica(id);
        //Assert
        verify(politicaCancelacionRepository).existsById(id);
        verify(politicaCancelacionRepository).deleteById(id);
    }

    @Test
    void eliminarPolitica_noExistePolitica_Test(){
        //Arrange
        Long id = 1L;

        when(politicaCancelacionRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            politicaCancelacionService.eliminarPolitica(id);
        });

        assertEquals("Política no encontrada", exception.getMessage());

        verify(politicaCancelacionRepository).existsById(id);
        verify(politicaCancelacionRepository, never()).deleteById(id);
    }

}
