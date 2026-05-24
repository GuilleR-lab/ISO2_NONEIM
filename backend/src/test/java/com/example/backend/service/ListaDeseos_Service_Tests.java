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
import com.example.backend.service.ListaDeseosService;

@ExtendWith(MockitoExtension.class)
public class ListaDeseos_Service_Tests {

    @Mock
    private ListaDeseosRepository listaDeseosRepository;

    @InjectMocks
    private ListaDeseosService listaDeseosService;

    @Test
    void crearLista_GuardadoExitoso_Test(){
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        ListaDeseos lista = new ListaDeseos(user);

        when(listaDeseosRepository.save(lista)).thenReturn(lista);

        //Act
        ListaDeseos resultado = listaDeseosService.crearLista(lista);

        //Assert
        assertNotNull(resultado);
        assertEquals(user.getId(), resultado.getUsuario().getId());

        verify(listaDeseosRepository).save(lista);
    }

    @Test
    void obtenerTodas_ExistenListas_Test(){
        //Arrange
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
        assertNotNull(resultado);
        assertEquals(metalista.size(), resultado.size());
        assertEquals(user1.getId(), resultado.get(0).getUsuario().getId());

        verify(listaDeseosRepository).findAll();
    }

    @Test
    void obtenerTodas_NoExisteLista_Test(){
        //Arrange
        when(listaDeseosRepository.findAll()).thenReturn(emptyList());
        //Act y Assert
        List <ListaDeseos> resultado = listaDeseosService.obtenerTodas();
        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        verify(listaDeseosRepository).findAll();
    }

    @Test
    void obtenerPorId_ExisteLista_Test(){
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        Long idLista = 2L;
        ListaDeseos lista = new ListaDeseos(user);
        ReflectionTestUtils.setField(lista,  "idLista", 2L);

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.of(lista));
        //Act
        Optional <ListaDeseos> resultado = listaDeseosService.obtenerPorId(idLista);

        //Assert
        assertNotNull(resultado);
        assertEquals(idLista, resultado.get().getIdLista());
        assertEquals(user.getId(), resultado.get().getUsuario().getId());

        verify(listaDeseosRepository).findById(idLista);

    }

    @Test
    void obtenerPorId_NoExisteLista_Test(){
        //Arrange
        Long idLista = 1L;

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.empty());
        //Act y Assert
        Optional <ListaDeseos> resultado = listaDeseosService.obtenerPorId(idLista);

        assertNotNull(resultado);
        assertEquals(Optional.empty(), resultado);

        verify(listaDeseosRepository).findById(idLista);
    }

    @Test
    void agregarInmueble_ListaExiste_Test(){
        //Arrange
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
        assertNotNull(resultado);
        assertEquals(inmueble.getIdInmueble(), resultado.getInmuebles().iterator().next().getIdInmueble());

        verify(listaDeseosRepository).findById(idLista);
        verify(listaDeseosRepository).save(lista);

    }

    @Test
    void agregarInmueble_NoExisteLista_Test(){
        //Arrange
        Inmueble inmueble = new Inmueble();
        Long idLista = 2L;

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.empty());
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            listaDeseosService.agregarInmueble(idLista, inmueble);
        });
            
        assertEquals("Lista no encontrada", exception.getMessage());

        verify(listaDeseosRepository, never()).save(any(ListaDeseos.class));
    }

    @Test
    void eliminarInmueble_ListaExiste_Test(){
        //Arrange
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
        assertNotNull(resultado);
        assertEquals(0, resultado.getInmuebles().size());

        verify(listaDeseosRepository).findById(idLista);
        verify(listaDeseosRepository).save(lista);
    }

    @Test
    void eliminarInmueble_NoExisteLista_Test(){
        //Arrange
        Inmueble inmueble = new Inmueble();
        Long idLista = 1L;

        when(listaDeseosRepository.findById(idLista)).thenReturn(Optional.empty());

        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            listaDeseosService.eliminarInmueble(idLista, inmueble);
        });

        assertEquals("Lista no encontrada", exception.getMessage());

        verify(listaDeseosRepository, never()).save(any(ListaDeseos.class));

    }

    @Test
    void eliminarLista_ExisteLista_Test(){
        //Arrange

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
    void eliminarLista_NoExisteLista_Test(){
        //Arrange
        Long idLista = 1L;

       when(listaDeseosRepository.existsById(idLista)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaDeseosService.eliminarLista(idLista);
        });

        assertEquals("No se puede eliminar: Lista no encontrada", exception.getMessage());
        verify(listaDeseosRepository, never()).deleteById(idLista);
    }

    
    
}
