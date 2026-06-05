package com.example.backend.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import com.example.backend.model.ListaDeseos;
import com.example.backend.model.Inmueble;
import com.example.backend.model.Usuario;
import com.example.backend.service.ListaDeseosService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListaDeseosController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ListaDeseosControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ListaDeseosService listaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCrearLista() throws Exception{
        //Arrange
        ListaDeseos lista = new ListaDeseos();
        Usuario user = new Usuario();
        user.setId(1L);
        lista.setUsuario(user);
        
        when(listaService.crearLista(any(ListaDeseos.class))).thenReturn(lista);
        //Act & Assert
        mockMvc.perform(post("/api/listas-deseos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lista)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario.id").value(1));
    }

    @Test
    public void testObtenerTodas() throws Exception{
        //Arrange
        ListaDeseos lista1 = new ListaDeseos();
        ListaDeseos lista2 = new ListaDeseos();
        List<ListaDeseos> listas = List.of(lista1, lista2);

        when(listaService.obtenerTodas()).thenReturn(listas);
        //Act & Assert
        mockMvc.perform(get("/api/listas-deseos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testObtenerPorId() throws Exception{
        //Arrange
        ListaDeseos lista = new ListaDeseos();
        Usuario user = new Usuario();
        user.setId(2L);
        lista.setUsuario(user);

        when(listaService.obtenerPorId(2L)).thenReturn(Optional.of(lista));
        //Act & Assert
        mockMvc.perform(get("/api/listas-deseos/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.usuario.id").value(2));
    }

    @Test
    public void testObtenerPorIdNotFound() throws Exception{
        when(listaService.obtenerPorId(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/listas-deseos/2"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testAgregarInmueble() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        Inmueble inmueble = new Inmueble();
        ListaDeseos lista = new ListaDeseos(user);

        when(listaService.agregarInmueble(eq(1L), any(Inmueble.class))).thenReturn(lista);

        //Act & Assert
        mockMvc.perform(put("/api/listas-deseos/1/agregar-inmueble")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inmueble)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.usuario.id").value(1));
    }

    @Test
    public void testEliminarInmueble() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        Inmueble inmueble = new Inmueble();
        ListaDeseos lista = new ListaDeseos(user);

        when(listaService.eliminarInmueble(eq(1L), any(Inmueble.class))).thenReturn(lista);
        //Act & Assert
        mockMvc.perform(put("/api/listas-deseos/1/eliminar-inmueble")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inmueble)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.usuario.id").value(1));
    }

    @Test
    public void testEliminarLista() throws Exception{
        //Arrange
        doNothing().when(listaService).eliminarLista(1L);
        //Act & Assert
        mockMvc.perform(delete("/api/listas-deseos/1"))
            .andExpect(status().isNoContent());
    }
}