package com.example.backend.controller;

import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.service.DisponibilidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisponibilidadController.class)
@AutoConfigureMockMvc(addFilters = false)
class DisponibilidadControllerTests {

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private DisponibilidadService dispoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testObtenerTodasLasDisponibilidades() throws Exception{
        //Arrange
        Inmueble inmueble1 = new Inmueble();
        Inmueble inmueble2 = new Inmueble();
        inmueble2.setIdInmueble(2L);
        inmueble1.setIdInmueble(1L);
        Disponibilidad dispo1 = new Disponibilidad(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 23), 100.0, true, inmueble1);
        Disponibilidad dispo2 = new Disponibilidad(LocalDate.of(2026, 3, 4), LocalDate.of(2026, 7, 28), 100.0, true, inmueble2);

        List <Disponibilidad> disponibilidades = List.of(dispo1, dispo2);
        when(dispoService.obtenerTodas()).thenReturn(disponibilidades);

        // Act & Assert
        mockMvc.perform(get("/api/disponibilidades")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerTodasLasDisponibilidadesVacia() throws Exception{
        //Arrange
        when(dispoService.obtenerTodas()).thenReturn(List.of());

        //Act & Assert
        mockMvc.perform(get("/api/disponibilidades")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testObtenerPorId() throws Exception{
        //Arrange
        Long id = 1L;
        Inmueble inmueble = new Inmueble();

        Disponibilidad dispo = new Disponibilidad(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 23), 100.0, true, inmueble);
        dispo.setIdDisponibilidad(1L);

        when(dispoService.obtenerPorId(id)).thenReturn(Optional.of(dispo));

        //Act & Assert
        mockMvc.perform(get("/api/disponibilidades/{id}", id)).andExpect(status().isOk())
            .andExpect(jsonPath("$.idDisponibilidad").value(1L));
    }

    @Test
    void testObtenerDisponibilidadPorIdNoExiste() throws Exception {
        when(dispoService.obtenerPorId(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/disponibilidades/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearNuevaDisponibilidad() throws Exception{
        //Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);
        Disponibilidad dispoNueva = new Disponibilidad(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 23), 100.0, true, inmueble);
        Disponibilidad dispoGuardada = new Disponibilidad(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 23), 100.0, true, inmueble);

        when(dispoService.crearDisponibilidad(any(Disponibilidad.class))).thenReturn(dispoGuardada);

        //Act & Assert
        mockMvc.perform(post("/api/disponibilidades")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dispoNueva)))
            .andExpect(status().isOk());
    }  
    
    @Test
    void testActualizarDisponibilidad() throws Exception{
        //Arrange
        Long id = 1L;
        Inmueble inmueble = new Inmueble();

        Disponibilidad dispoActualizada = new Disponibilidad(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 23), 100.0, true, inmueble);
        dispoActualizada.setIdDisponibilidad(id);

        Disponibilidad dispoExistente = new Disponibilidad(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 5, 23), 80.0, false, inmueble);
        dispoExistente.setIdDisponibilidad(id);

        when(dispoService.actualizarDisponibilidad(eq(id), any(Disponibilidad.class))).thenReturn(dispoActualizada);

        //Act & Assert
        mockMvc.perform(put("/api/disponibilidades/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dispoActualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.precio").value(100.0));
    }

    @Test
    void testActualizarDisponibilidadNotFound() throws Exception {
        // Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);

        Disponibilidad dispoActualizada = new Disponibilidad(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 23),
                100.0,
                true,
                inmueble
        );

        when(dispoService.actualizarDisponibilidad(eq(1L), any(Disponibilidad.class)))
                .thenThrow(new RuntimeException());

        // Act + Assert
        mockMvc.perform(put("/api/disponibilidades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dispoActualizada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarDisponibilidad() throws Exception{
        // Arrange
        doNothing().when(dispoService).eliminarDisponibilidad(1L);

        // Act + Assert
        mockMvc.perform(delete("/api/disponibilidades/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarDisponibilidadNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException())
                .when(dispoService).eliminarDisponibilidad(1L);

        // Act + Assert
        mockMvc.perform(delete("/api/disponibilidades/1"))
                .andExpect(status().isNotFound());
    }
}