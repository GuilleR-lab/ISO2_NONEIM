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

import com.example.backend.model.Pago;
import com.example.backend.service.PagoService;

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class Pago_controller_Tests {
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PagoService pagoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void crearPago_Test() throws Exception {
        //Arrange
        Pago pago = new Pago();
        pago.setReferencia(1L);
        pago.setImporte(100.0);

        when(pagoService.crearPago(any(Pago.class))).thenReturn(pago);
        //Act & Assert
        mockMvc.perform(post("/api/pagos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referencia").value(1L))
                .andExpect(jsonPath("$.importe").value(100.0));
    }

    @Test
    public void obtenerTodos_Test() throws Exception{
        //Arrange
        Pago pago1 = new Pago();
        Pago pago2 = new Pago();
        pago1.setReferencia(1L);
        pago2.setReferencia(2L);

        List<Pago> pagos = List.of(pago1, pago2);
        when(pagoService.obtenerTodos()).thenReturn(pagos);
        //Act & Assert
        mockMvc.perform(get("/api/pagos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    public void obtenerPorReferencia_Test() throws Exception{
        //Arrange
        Pago pago = new Pago();
        pago.setReferencia(1L);

        when(pagoService.obtenerPorReferencia(1L)).thenReturn(Optional.of(pago));
        //Act & Assert
        mockMvc.perform(get("/api/pagos/{referencia}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.referencia").value(1L));
    }

    @Test
    public void obtenerPorReferencia_NotFound_Test() throws Exception{
        //Arrange
        when(pagoService.obtenerPorReferencia(1L)).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(get("/api/pagos/{referencia}", 1L))
            .andExpect(status().isNotFound());
    }

    @Test
    public void actualizarPago_Test() throws Exception{
        //Arrange
        Pago pagoActualizado = new Pago();
        pagoActualizado.setReferencia(1L);
        pagoActualizado.setImporte(150.0);

        when(pagoService.actualizarPago(eq(1L), any(Pago.class))).thenReturn(pagoActualizado);
        //Act & Assert
        mockMvc.perform(put("/api/pagos/{referencia}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pagoActualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importe").value(150.0));
    }

    @Test
    public void actualizarPago_NotFound_Test() throws Exception{
        Pago pagoActualizado = new Pago();
        pagoActualizado.setImporte(150.0);

        when(pagoService.actualizarPago(eq(1L), any(Pago.class))).thenThrow(new RuntimeException("Pago no encontrado"));
        //Act & Assert
        mockMvc.perform(put("/api/pagos/{referencia}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pagoActualizado)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void eliminarPago_Test() throws Exception{
        //Arrange
        Pago pago = new Pago();
        pago.setReferencia(1L);

        doNothing().when(pagoService).eliminarPago(1L);
        //Act & Assert
        mockMvc.perform(delete("/api/pagos/{referencia}", 1L))
            .andExpect(status().isNoContent());
    }

}
