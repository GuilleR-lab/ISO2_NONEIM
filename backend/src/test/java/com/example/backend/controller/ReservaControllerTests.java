package com.example.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.backend.model.SolicitudReserva;
import com.example.backend.model.Reserva;
import com.example.backend.model.Usuario;
import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.model.Pago;
import com.example.backend.service.ReservaService;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.repository.InmuebleRepository;
import com.example.backend.repository.PagoRepository;
import com.example.backend.repository.ReservaRepository;
import com.example.backend.repository.SolicitudReservaRepository;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTests {
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservaService reservaService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private InmuebleRepository inmuebleRepository;

    @MockitoBean
    private PagoRepository pagoRepository; // Restaurado para evitar el fallo de carga del contexto de Spring

    @MockitoBean
    private SolicitudReservaRepository solicitudReservaRepository;

    @MockitoBean
    private ReservaRepository reservaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testReservarNoExisteUsuario() throws Exception{
        //Arrange
        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 2L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-10",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    void testReservarNoExisteInmueble() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 2L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-10",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(inmuebleRepository.findById(2L)).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Inmueble no encontrado"));
    }

    @Test
    void testReservarPropioInmueble() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(2L);
        inmueble.setPropietario(user);

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 2L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-10",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(inmuebleRepository.findById(2L)).thenReturn(Optional.of(inmueble));
        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("No puedes reservar tu propio inmueble"));
    }

    @Test
    void testReservarSinDisponibilidad() throws Exception{
        //Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Usuario propietario = new Usuario();
        propietario.setId(99L);

        Inmueble inmueble = new Inmueble();
        inmueble.setPropietario(propietario);
        inmueble.setDisponibilidades(List.of());

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 2L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-10",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(inmuebleRepository.findById(2L)).thenReturn(Optional.of(inmueble));
        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
            .value("No hay disponibilidad para esas fechas"));
    }

    @Test
    void testReservarCeroDias() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFechaInicio(LocalDate.of(2024, 7, 1));
        disponibilidad.setFechaFin(LocalDate.of(2024, 7, 31));
        disponibilidad.setDirecta(true);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);
        inmueble.setPropietario(new Usuario() {{ setId(99L); }});
        inmueble.setDisponibilidades(List.of(disponibilidad));
        inmueble.setPrecioNoche(100); // Devuelto a Integer por si acaso

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 1L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-01",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(inmuebleRepository.findById(1L)).thenReturn(Optional.of(inmueble));
        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Las fechas no son válidas"));
    }

    @Test
    void testReservarReservasSolapadas() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFechaInicio(LocalDate.of(2024, 7, 1));
        disponibilidad.setFechaFin(LocalDate.of(2024, 7, 31));
        disponibilidad.setDirecta(true);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);
        inmueble.setPropietario(new Usuario() {{ setId(99L); }});
        inmueble.setDisponibilidades(List.of(disponibilidad));
        inmueble.setPrecioNoche(100);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setFechaInicio(LocalDate.of(2024, 7, 5));
        reservaExistente.setFechaFin(LocalDate.of(2024, 7, 10));

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 1L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-15",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(inmuebleRepository.findById(1L)).thenReturn(Optional.of(inmueble));
        when(reservaRepository.findByInmuebleId(1L)).thenReturn(List.of(reservaExistente));
        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Ya existe una reserva para esas fechas"));
    }

    @Test
    void testReservarReservaDirecta() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFechaInicio(LocalDate.of(2024, 7, 1));
        disponibilidad.setFechaFin(LocalDate.of(2024, 7, 31));
        disponibilidad.setDirecta(true);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);
        inmueble.setPropietario(new Usuario() {{ setId(99L); }});
        inmueble.setDisponibilidades(List.of(disponibilidad));
        inmueble.setPrecioNoche(100);

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 1L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-10",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(inmuebleRepository.findById(1L)).thenReturn(Optional.of(inmueble));
        when(reservaRepository.findByInmuebleId(1L)).thenReturn(List.of());

        Reserva reservaCreada = new Reserva();
        reservaCreada.setIdReserva(1L);
        reservaCreada.setFechaInicio(LocalDate.of(2024, 7, 1));
        reservaCreada.setFechaFin(LocalDate.of(2024, 7, 10));
        reservaCreada.setActiva(true);
        reservaCreada.setInquilino(user);
        reservaCreada.setInmueble(inmueble);

        when(reservaService.crearReserva(any(Reserva.class))).thenReturn(reservaCreada);

        //Act & Assert
        mockMvc.perform(post("/api/reservas/reservar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idReserva").value(1L))
            .andExpect(jsonPath("$.tipo").value("DIRECTA"));
    }

    @Test
    void testReservarSolicitudReserva() throws Exception{
        //ARRANGE
        Usuario user = new Usuario();
        user.setId(1L);

        Usuario propietario = new Usuario();
        propietario.setId(99L);

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFechaInicio(LocalDate.of(2024, 7, 1));
        disponibilidad.setFechaFin(LocalDate.of(2024, 7, 31));
        disponibilidad.setDirecta(false);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);
        inmueble.setPropietario(propietario);
        inmueble.setDisponibilidades(List.of(disponibilidad));
        inmueble.setPrecioNoche(100);

        SolicitudReserva solicitudGuardada = new SolicitudReserva();
        solicitudGuardada.setIdSolicitud(10L);

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setIdReserva(20L);

        Map<String, Object> request = Map.of(
            "inquilinoId", 1L,
            "inmuebleId", 1L,
            "fechaInicio", "2024-07-01",
            "fechaFin", "2024-07-10",
            "metodoPago", "TARJETA"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(inmuebleRepository.findById(1L)).thenReturn(Optional.of(inmueble));
        when(reservaRepository.findByInmuebleId(1L)).thenReturn(List.of());

        when(solicitudReservaRepository.save(any(SolicitudReserva.class))).thenReturn(solicitudGuardada);
        when(reservaService.crearReserva(any(Reserva.class))).thenReturn(reservaGuardada);

        //ACT & ASSERT
        mockMvc.perform(post("/api/reservas/reservar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value("Solicitud enviada al propietario, pendiente de confirmación"))
            .andExpect(jsonPath("$.tipo").value("SOLICITUD"))
            .andExpect(jsonPath("$.idSolicitud").value(10L))
            .andExpect(jsonPath("$.importe").value(900.0));
    }

    @Test
    void testObtenerPorInquilino() throws Exception {
        //ARRANGE
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(10L);
        inmueble.setCiudad("Madrid");
        inmueble.setDireccion("Calle A");

        Reserva reserva = new Reserva();
        reserva.setIdReserva(100L);
        reserva.setFechaInicio(LocalDate.of(2024, 7, 1));
        reserva.setFechaFin(LocalDate.of(2024, 7, 10));
        reserva.setPagado(true);
        reserva.setActiva(true);
        reserva.setInmueble(inmueble);

        Pago pago = new Pago();
        pago.setMetodoPago("TARJETA");
        reserva.setPago(pago);

        when(reservaService.obtenerPorInquilino(1L)).thenReturn(List.of(reserva));

        //ACT & ASSERT
        mockMvc.perform(get("/api/reservas/inquilino/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idReserva").value(100))
            .andExpect(jsonPath("$[0].fechaInicio").value("2024-07-01"))
            .andExpect(jsonPath("$[0].fechaFin").value("2024-07-10"))
            .andExpect(jsonPath("$[0].pagado").value(true))
            .andExpect(jsonPath("$[0].activa").value(true))
            .andExpect(jsonPath("$[0].inmueble.idInmueble").value(10))
            .andExpect(jsonPath("$[0].inmueble.ciudad").value("Madrid"))
            .andExpect(jsonPath("$[0].inmueble.direccion").value("Calle A"))
            .andExpect(jsonPath("$[0].pago.metodoPago").value("TARJETA"));
    }

    @Test
    void testObtenerTodas() throws Exception{
        //Arrange
        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();
        reserva1.setIdReserva(1L);
        reserva2.setIdReserva(2L);
        List<Reserva> reservas = List.of(reserva1, reserva2);
        when(reservaService.obtenerTodas()).thenReturn(reservas);

        //Act & Assert
        mockMvc.perform(get("/api/reservas")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerPorId() throws Exception{
        //Arrange
        Reserva reserva = new Reserva();
        reserva.setIdReserva(1L);
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));

        //Act & Assert
        mockMvc.perform(get("/api/reservas/{id}", 1L)).andExpect(status().isOk())
            .andExpect(jsonPath("$.idReserva").value(1L));
    }

    @Test
    void testObtenerPorIdNotFound() throws Exception{
        //Arrange
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(get("/api/reservas/{id}", 1L)).andExpect(status().isNotFound());
    }

    @Test
    void testEliminarReserva() throws Exception {
        //Arrange
        doNothing().when(reservaService).eliminarReserva(1L);

        //Act & Assert
        mockMvc.perform(delete("/api/reservas/{id}", 1L)).andExpect(status().isNoContent());
        verify(reservaService, times(1)).eliminarReserva(1L);
    }
}