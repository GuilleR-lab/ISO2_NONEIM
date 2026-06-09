package com.example.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

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

import com.example.backend.model.SolicitudReserva;
import com.example.backend.service.SolicitudReservaService;

@WebMvcTest(SolicitudReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SolicitudReserva_controller_Tests {
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolicitudReservaService solicitudService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void crearSolicitud_Test() throws Exception {
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
    public void obtenerTodas_Test() throws Exception{
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
    public void obtenerPorId_Test() throws Exception{
        //Arrange
        SolicitudReserva solicitud = new SolicitudReserva();
        solicitud.setIdSolicitud(1L);
        when(solicitudService.obtenerPorId(1L)).thenReturn(Optional.of(solicitud));

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/inquilino/{id}", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.idSolicitud").value(1L));
    }

    @Test
    public void obtenerPorId_NotFound_Test() throws Exception{
        //Arrange
        when(solicitudService.obtenerPorId(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/inquilino/{id}", 1L)).andExpect(status().isNotFound());
    }

    @Test
    public void obtenerPorInquilinoId_Test() throws Exception {
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
    public void actualizarSolicitud_Test() throws Exception {
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
    public void actualizarSolicitud_NotFound_Test() throws Exception {
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
    public void eliminarSolicitud_Test() throws Exception {
        //Arrange
        doNothing().when(solicitudService).eliminarSolicitud(1L);

        //Act & Assert
        mockMvc.perform(delete("/api/solicitudes/inquilino/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    public void obtenerPendientes_Test() throws Exception {
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
    public void contarPendientes_Test() throws Exception {
        //Arrange
        when(solicitudService.contarPendientesPropietario(1L)).thenReturn(5L);

        //Act & Assert
        mockMvc.perform(get("/api/solicitudes/propietario/{propietarioId}/pendientes/count", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(5L));
    }

    @Test
    public void actualizarEstado_Test() throws Exception {
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
    public void actualizarEstado_InvalidState_Test() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/solicitudes/{id}/estado", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("estado", "INVALIDO"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Estado inválido"));
    }

    @Test
    public void actualizarEstado_NotFound_Test() throws Exception {
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
    public void actualizarEstado_ErrorInterno_Test() throws Exception {
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
