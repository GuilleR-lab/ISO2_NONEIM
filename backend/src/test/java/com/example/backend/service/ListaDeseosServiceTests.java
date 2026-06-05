package com.example.backend.service;

import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.example.backend.model.Inmueble;
import com.example.backend.model.ListaDeseos;
import com.example.backend.model.Usuario;
import com.example.backend.repository.ListaDeseosRepository;

@ExtendWith(MockitoExtension.class)
public class ListaDeseosServiceTests {

    @Mock
    private ListaDeseosRepository listaDeseosRepository;

    @InjectMocks
    private ListaDeseosService listaDeseosService;

    @Test
    void testCrearListaGuardadoExitoso(){        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        ListaDeseos lista = new ListaDeseos(user);

        when(listaDeseosRepository.save(lista)).thenReturn(lista);

        //Act
        ListaDeseos resultado = listaDeseosService.crearLista(lista);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(user.getId(), resultado.getUsuario().getId(), "verificacion");

        verify(listaDeseosRepository).save(lista);
    }

    @Test
    void testObtenerTodasExistenListas(){        //Arrange
        Usuario user1 = new Usuario();
        Usuario user2 = new Usuario();
        user1.setId(1L);
        user2.setId(2L);
        ListaDeseos lista1 = new ListaDeseos(user1);
        ListaDeseos lista2 = new ListaDeseos(user2);

        List <ListaDeseos> metalista = List.of(lista1, lista2);
        when(listaDeseosRepository.findAll()).thenReturn(metalista);
        //Act
        List <ListaDeseos> resultado = listaDeseosService.obtenerTodas();
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(metalista.size(), resultado.size(), "verificacion");
        assertEquals(user1.getId(), resultado.get(0).getUsuario().getId(), "verificacion");

        verify(listaDeseosRepository).findAll();
    }

    @Test
    void testObtenerTodasNoExisteLista(){        //Arrange
        when(listaDeseosRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <ListaDeseos> resultado = listaDeseosService.obtenerTodas();
        assertNotNull(resultado, "verificacion");
        assertEquals(0, resultado.size(), "verificacion");

        verify(listaDeseosRepository).findAll();
    }

    @Test
    void testObtenerPorIdExisteLista(){        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        Long idLista = 2L;
        ListaDeseos lista = new ListaDeseos(user);
        ReflectionTestUtils.setField(lista,  "idLista", 2L);

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.of(lista));
        //Act
        Optional <ListaDeseos> resultado = listaDeseosService.obtenerPorId(idLista);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(idLista, resultado.get().getIdLista(), "verificacion");
        assertEquals(user.getId(), resultado.get().getUsuario().getId(), "verificacion");

        verify(listaDeseosRepository).findById(idLista);

    }

    @Test
    void testObtenerPorIdNoExisteLista(){        //Arrange
        Long idLista = 1L;

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <ListaDeseos> resultado = listaDeseosService.obtenerPorId(idLista);

        assertNotNull(resultado, "verificacion");
        assertEquals(Optional.empty(), resultado, "verificacion");

        verify(listaDeseosRepository).findById(idLista);
    }

    @Test
    void testAgregarInmuebleListaExiste(){        //Arrange
        Usuario user = new Usuario();
        Inmueble inmueble = new Inmueble();
        user.setId(1L);
        inmueble.setIdInmueble(3L);

        ListaDeseos lista = new ListaDeseos(user);
        Long idLista = 2L;
        ReflectionTestUtils.setField(lista, "idLista", idLista);
        
       
        
        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.of(lista));
        when(listaDeseosRepository.save(lista)).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        ListaDeseos resultado = listaDeseosService.agregarInmueble(idLista, inmueble);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(inmueble.getIdInmueble(), resultado.getInmuebles().iterator().next().getIdInmueble(), "verificacion");

        verify(listaDeseosRepository).findById(idLista);
        verify(listaDeseosRepository).save(lista);

    }

    @Test
    void testAgregarInmuebleNoExisteLista(){        //Arrange
        Inmueble inmueble = new Inmueble();
        Long idLista = 2L;

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            listaDeseosService.agregarInmueble(idLista, inmueble);
        }, "verificacion");
            
        assertEquals("Lista no encontrada", exception.getMessage(), "verificacion");

        verify(listaDeseosRepository, never()).save(any(ListaDeseos.class));
    }

    @Test
    void testEliminarInmuebleListaExiste(){        //Arrange
        Inmueble inmueble = new Inmueble();
        Usuario user = new Usuario();
        ListaDeseos lista = new ListaDeseos();
        lista.setUsuario(user);
        user.setId(1L);
        Long idLista = 2L;
        ReflectionTestUtils.setField(lista, "idLista", idLista);

        lista.getInmuebles().add(inmueble);

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.of(lista));
        when(listaDeseosRepository.save(lista)).thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        ListaDeseos resultado = listaDeseosService.eliminarInmueble(idLista, inmueble);

        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(0, resultado.getInmuebles().size(), "verificacion");

        verify(listaDeseosRepository).findById(idLista);
        verify(listaDeseosRepository).save(lista);
    }

    @Test
    void testEliminarInmuebleNoExisteLista(){        //Arrange
        Inmueble inmueble = new Inmueble();
        Long idLista = 1L;

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.empty());

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            listaDeseosService.eliminarInmueble(idLista, inmueble);
        }, "verificacion");

        assertEquals("Lista no encontrada", exception.getMessage(), "verificacion");

        verify(listaDeseosRepository, never()).save(any(ListaDeseos.class));

    }

    @Test
    void testEliminarListaExisteLista(){        //Arrange

        Long idLista = 1L;

        when(listaDeseosRepository.existsById(idLista)).thenReturn(true);
        //Act
        listaDeseosService.eliminarLista(idLista);
        
        //Assert
        verify(listaDeseosRepository).existsById(idLista);
        verify(listaDeseosRepository).deleteById(idLista);
        verifyNoMoreInteractions(listaDeseosRepository);
      
    }

    @Test
    void testEliminarListaNoExisteLista(){        //Arrange
        Long idLista = 1L;

       when(listaDeseosRepository.existsById(idLista)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaDeseosService.eliminarLista(idLista);
        }, "verificacion");

        assertEquals("No se puede eliminar: Lista no encontrada", exception.getMessage(), "verificacion");
        verify(listaDeseosRepository, never()).deleteById(idLista);
    }

    
    
}