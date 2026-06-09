package com.example.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.backend.model.SolicitudReserva;
import com.example.backend.service.SolicitudReservaService;

@WebMvcTest(SolicitudReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class SolicitudReservaControllerTests {
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudReservaService solicitudService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCrearSolicitud() throws Exception {
        //Arrange
        SolicitudReserva solicitud = new SolicitudReserva();
        solicitud.setIdSolicitud(1L);
        solicitud.setEstado("PENDIENTE");

        when(solicitudService.crearSolicitud(any(SolicitudReserva.class))).thenReturn(solicitud);
        //Act & Assert
        mockMvc.perform(post("/api/solicitudes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(solicitud)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idSolicitud").value(1L))
            .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testObtenerTodas() throws Exception{
        //Arrange
        SolicitudReserva solicitud1 = new SolicitudReserva();
        SolicitudReserva solicitud2 = new SolicitudReserva();
        solicitud1.setIdSolicitud(1L);
        solicitud2.setIdSolicitud(2L);
        List<SolicitudReserva> solicitudes = List.of(solicitud1, solicitud2);
        when(solicitudService.obtenerTodas()).thenReturn(solicitudes);

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerPorId() throws Exception{
        //Arrange
        SolicitudReserva solicitud = new SolicitudReserva();
        solicitud.setIdSolicitud(1L);
        when(solicitudService.obtenerPorId(1L)).thenReturn(Optional.of(solicitud));

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/inquilino/{id}", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.idSolicitud").value(1L));
    }

    @Test
    void testObtenerPorIdNotFound() throws Exception{
        //Arrange
        when(solicitudService.obtenerPorId(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/inquilino/{id}", 1L)).andExpect(status().isNotFound());
    }

    @Test
    void testObtenerPorInquilinoId() throws Exception {
        //Arrange
        SolicitudReserva solicitud1 = new SolicitudReserva();
        SolicitudReserva solicitud2 = new SolicitudReserva();
        solicitud1.setIdSolicitud(1L);
        solicitud2.setIdSolicitud(2L);
        List<SolicitudReserva> solicitudes = List.of(solicitud1, solicitud2);
        when(solicitudService.findByUsuarioId(1L)).thenReturn(solicitudes);

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/inquilino/usuario/{inquilinoId}", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testActualizarSolicitud() throws Exception {
        //Arrange
        SolicitudReserva solicitudActualizada = new SolicitudReserva();
        solicitudActualizada.setIdSolicitud(1L);
        solicitudActualizada.setEstado("ACEPTADA");

        when(solicitudService.actualizarSolicitud(eq(1L), any(SolicitudReserva.class))).thenReturn(solicitudActualizada);
        //Act & Assert
        mockMvc.perform(put("/api/solicitudes/inquilino/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(solicitudActualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idSolicitud").value(1L))
            .andExpect(jsonPath("$.estado").value("ACEPTADA"));
    }

    @Test
    void testActualizarSolicitudNotFound() throws Exception {
        //Arrange
        SolicitudReserva solicitudActualizada = new SolicitudReserva();
        solicitudActualizada.setIdSolicitud(1L);
        solicitudActualizada.setEstado("ACEPTADA");

        when(solicitudService.actualizarSolicitud(eq(1L), any(SolicitudReserva.class))).thenThrow(new RuntimeException("Solicitud no encontrada"));

        //Act & Assert
        mockMvc.perform(put("/api/solicitudes/inquilino/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(solicitudActualizada)))
            .andExpect(status().isNotFound());
    }   
    
    @Test
    void testEliminarSolicitud() throws Exception {
        //Arrange
        doNothing().when(solicitudService).eliminarSolicitud(1L);

        //Act & Assert
        mockMvc.perform(delete("/api/solicitudes/inquilino/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerPendientes() throws Exception {
        //Arrange
        SolicitudReserva solicitud1 = new SolicitudReserva();
        SolicitudReserva solicitud2 = new SolicitudReserva();
        solicitud1.setIdSolicitud(1L);
        solicitud2.setIdSolicitud(2L);
        List<SolicitudReserva> solicitudes = List.of(solicitud1, solicitud2);
        when(solicitudService.obtenerPendientesPropietario(1L)).thenReturn(solicitudes);

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/propietario/{propietarioId}/pendientes", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testContarPendientes() throws Exception {
        //Arrange
        when(solicitudService.contarPendientesPropietario(1L)).thenReturn(5L);

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/propietario/{propietarioId}/pendientes/count", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    void testActualizarEstado() throws Exception {
        //Arrange
        SolicitudReserva solicitudActualizada = new SolicitudReserva();
        solicitudActualizada.setIdSolicitud(1L);
        solicitudActualizada.setEstado("ACEPTADA");

        when(solicitudService.cambiarEstadoSolicitud(eq(1L), eq("ACEPTADA"))).thenReturn(solicitudActualizada);
        //Act & Assert
        mockMvc.perform(patch("/api/solicitudes/{id}/estado", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("estado", "ACEPTADA"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idSolicitud").value(1L))
            .andExpect(jsonPath("$.estado").value("ACEPTADA"));
    }

    @Test
    void testActualizarEstadoInvalidState() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/solicitudes/{id}/estado", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("estado", "INVALIDO"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Estado inválido"));
    }

    @Test
    void testActualizarEstadoNotFound() throws Exception {
        //Arrange
        when(solicitudService.cambiarEstadoSolicitud(eq(1L), eq("ACEPTADA"))).thenThrow(new RuntimeException("Solicitud no encontrada"));

        //Act & Assert
        mockMvc.perform(patch("/api/solicitudes/{id}/estado", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("estado", "ACEPTADA"))))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Solicitud no encontrada"));
    }

    @Test
    void testActualizarEstadoErrorInterno() throws Exception {
        //Arrange
        doAnswer(invocation -> { throw new Exception("Error inesperado"); })
            .when(solicitudService).cambiarEstadoSolicitud(eq(1L), eq("ACEPTADA"));

        //Act & Assert
        mockMvc.perform(patch("/api/solicitudes/{id}/estado", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("estado", "ACEPTADA"))))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Error interno del servidor"));
    }
}