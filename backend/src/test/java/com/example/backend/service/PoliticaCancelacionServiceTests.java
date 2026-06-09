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

@ExtendWith(MockitoExtension.class)
class PoliticaCancelacionServiceTests {
    
    @Mock
    private PoliticaCancelacionRepository politicaCancelacionRepository;

    @InjectMocks
    private PoliticaCancelacionService politicaCancelacionService;

    @Test
    void testCrearPoliticaGuardadoExitoso(){        //Arrange
        PoliticaCancelacion politica = new PoliticaCancelacion();

        when(politicaCancelacionRepository.save(politica)).thenReturn(politica);
        //Act
        PoliticaCancelacion politicaGuardada = politicaCancelacionService.crearPolitica(politica);
        //Assert
        assertNotNull(politicaGuardada, "verificacion");
        assertEquals(politica, politicaGuardada, "verificacion");
    }

    @Test
    void testObtenerTodasExistenPoliticas(){        //Arrange
        PoliticaCancelacion politica1 = new PoliticaCancelacion();
        PoliticaCancelacion politica2 = new PoliticaCancelacion();

        List <PoliticaCancelacion> politicas = List.of(politica1, politica2);

        when(politicaCancelacionRepository.findAll()).thenReturn(politicas);
        //Act 
        List <PoliticaCancelacion> politicasObtenidas = politicaCancelacionService.obtenerTodas();
        //Assert
        assertNotNull(politicasObtenidas, "verificacion");
        assertEquals(politicas.size(), politicasObtenidas.size(), "verificacion");
        assertEquals(politicas.get(0), politicasObtenidas.get(0), "verificacion");

        verify(politicaCancelacionRepository).findAll();
    }

    @Test
    void testObtenerTodasNoExistenPoliticas(){        //Arrange
        when(politicaCancelacionRepository.findAll()).thenReturn(List.of());
        //Act
        List <PoliticaCancelacion> politicasObtenidas = politicaCancelacionService.obtenerTodas();
        //Assert
        assertNotNull(politicasObtenidas, "verificacion");
        assertTrue(politicasObtenidas.isEmpty(), "verificacion");

        verify(politicaCancelacionRepository).findAll();
    }

    @Test
    void testObtenerPorIdExistePolitica(){        //Arrange
        Long id = 1L;
        PoliticaCancelacion politica = new PoliticaCancelacion("Descripcion", 10.0);
        politica.setIdPolitica(id);

        when(politicaCancelacionRepository.findById(id)).thenReturn(java.util.Optional.of(politica));
        //Act
        Optional <PoliticaCancelacion> politicaObtenida = politicaCancelacionService.obtenerPorId(id);

        //Assert
        assertNotNull(politicaObtenida, "verificacion");
        assertEquals(politica.getDescripcion(), politicaObtenida.get().getDescripcion(), "verificacion");

        verify(politicaCancelacionRepository).findById(id);
    }

    @Test
    void testObtenerPorIdNoExistePolitica(){        //Arrange
        Long id = 1L;

        when(politicaCancelacionRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <PoliticaCancelacion> politicaObtenida = politicaCancelacionService.obtenerPorId(id);

        //Assert
        assertNotNull(politicaObtenida, "verificacion");
        assertTrue(politicaObtenida.isEmpty(), "verificacion");

        verify(politicaCancelacionRepository).findById(id);
    }

    @Test
    void testActualizarPoliticaExistePolitica(){        //Arrange
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
        assertNotNull(politicaActualizada, "verificacion");
        assertEquals(nuevaPenalizacion, politicaActualizada.getPenalizacion(), "verificacion");
        assertEquals(politicaOriginal.getDescripcion(), politicaActualizada.getDescripcion(), "verificacion");

        verify(politicaCancelacionRepository).findById(id);
        verify(politicaCancelacionRepository).save(any(PoliticaCancelacion.class));

    }

    @Test
    void testActualizarPoliticaNoExistePolitica(){        //Arrange
        Long id = 1L;

        PoliticaCancelacion nuevaPolitica = new PoliticaCancelacion("Descripcion", 10.0);

        when(politicaCancelacionRepository.findById(id)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            politicaCancelacionService.actualizarPolitica(id, nuevaPolitica);
        }, "verificacion");

        assertEquals("Política no encontrada", exception.getMessage(), "verificacion");

        verify(politicaCancelacionRepository, never()).save(any(PoliticaCancelacion.class));
    }

    @Test
    void testEliminarPoliticaExistePolitica(){        //Arrange
        Long id = 1L;
        
        when(politicaCancelacionRepository.existsById(id)).thenReturn(true);
        //Act
        politicaCancelacionService.eliminarPolitica(id);
        //Assert
        verify(politicaCancelacionRepository).existsById(id);
        verify(politicaCancelacionRepository).deleteById(id);
    }

    @Test
    void testEliminarPoliticaNoExistePolitica(){        //Arrange
        Long id = 1L;

        when(politicaCancelacionRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            politicaCancelacionService.eliminarPolitica(id);
        }, "verificacion");

        assertEquals("Política no encontrada", exception.getMessage(), "verificacion");

        verify(politicaCancelacionRepository).existsById(id);
        verify(politicaCancelacionRepository, never()).deleteById(id);
    }

}