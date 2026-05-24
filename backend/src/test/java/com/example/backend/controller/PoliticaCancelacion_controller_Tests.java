package com.example.backend.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.backend.model.PoliticaCancelacion;
import com.example.backend.service.PoliticaCancelacionService;

@WebMvcTest(PoliticaCancelacionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PoliticaCancelacion_controller_Tests {
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PoliticaCancelacionService politicaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void crearPolitica_Test() throws Exception{
        //Arrange
        PoliticaCancelacion pol = new PoliticaCancelacion();
        pol.setDescripcion("Política de cancelación flexible");

        when(politicaService.crearPolitica(any(PoliticaCancelacion.class))).thenReturn(pol);
        //Act & Assert
        mockMvc.perform(post("/api/politicas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pol)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descripcion").value("Política de cancelación flexible"));
    }

    @Test
    public void obtenerTodas_Test() throws Exception{
        //Arrange
        PoliticaCancelacion pol1 = new PoliticaCancelacion();
        PoliticaCancelacion pol2 = new PoliticaCancelacion();
        List<PoliticaCancelacion> politicas = List.of(pol1, pol2);

        when(politicaService.obtenerTodas()).thenReturn(politicas);
        //Act & Assert
        mockMvc.perform(get("/api/politicas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void obtenerPorId_Test() throws Exception{
        //Arrange
        PoliticaCancelacion pol = new PoliticaCancelacion();
        pol.setIdPolitica(1L);
        when(politicaService.obtenerPorId(1L)).thenReturn(Optional.of(pol));
        //Act & Assert
        mockMvc.perform(get("/api/politicas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idPolitica").value(1L));
    }

    @Test
    public void obtenerPorId_NotFound_Test() throws Exception{
        //Arrange
        when(politicaService.obtenerPorId(1L)).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(get("/api/politicas/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void actualizarPolitica_Test() throws Exception{
        //Arrange
        PoliticaCancelacion polActualizada = new PoliticaCancelacion();
        polActualizada.setDescripcion("Política de cancelación estricta");

        PoliticaCancelacion polGuardada = new PoliticaCancelacion();
        polGuardada.setIdPolitica(1L);
        polGuardada.setDescripcion("Política de cancelación estricta");

        when(politicaService.actualizarPolitica(eq(1L), any(PoliticaCancelacion.class))).thenReturn(polGuardada);

        //Act & Assert
        mockMvc.perform(put("/api/politicas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(polActualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idPolitica").value(1L))
            .andExpect(jsonPath("$.descripcion").value("Política de cancelación estricta"));
    }

    @Test
    public void actualizarPolitica_NotFound_Test() throws Exception{
        PoliticaCancelacion polActualizada = new PoliticaCancelacion();
        polActualizada.setDescripcion("Política de cancelación estricta");

        when(politicaService.actualizarPolitica(eq(1L), any(PoliticaCancelacion.class))).thenThrow(new RuntimeException("Política no encontrada"));

        mockMvc.perform(put("/api/politicas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(polActualizada)))
            .andExpect(status().isNotFound());
    }
}
