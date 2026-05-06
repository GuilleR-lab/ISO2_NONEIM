package com.example.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudReservaTest {

    private Usuario usuario;
    private Disponibilidad disponibilidad;

    @BeforeEach
    void setUp() {
        Direccion dir = new Direccion("España", "Bilbao", "48001", "Gran Vía", "12", null);
        usuario = new Usuario("user1", "Elena", "Torres", "elena@example.com", "pass", dir, Usuario.Rol.INQUILINO);
        Usuario propietario = new Usuario("prop1", "Roberto", "Vega", "roberto@example.com", "pass", dir, Usuario.Rol.PROPIETARIO);
        Inmueble inmueble = new Inmueble("C/ Ercilla", "Bilbao", 55.0, Inmueble.Tipo.HABITACION, "Habitación tranquila", propietario);
        disponibilidad = new Disponibilidad(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 30), 55.0, false, inmueble);
    }

    @Test
    void testConstructorVacioYFechaSolicitudPorDefecto() {
        SolicitudReserva s = new SolicitudReserva();
        assertNotNull(s.getFechaSolicitud(), "La fecha de solicitud debe inicializarse automáticamente");
        assertNull(s.getEstado(), "El estado debe ser nulo en constructor vacío");
    }

    @Test
    void testSettersYGetters() {
        SolicitudReserva s = new SolicitudReserva();
        s.setIdSolicitud(1L);
        s.setEstado("PENDIENTE");
        s.setFechaInicio(LocalDate.of(2025, 9, 5));
        s.setFechaFin(LocalDate.of(2025, 9, 10));
        s.setUsuario(usuario);
        s.setDisponibilidad(disponibilidad);

        assertEquals(1L, s.getIdSolicitud(), "El id de la solicitud debe ser 1");
        assertEquals("PENDIENTE", s.getEstado(), "El estado debe ser PENDIENTE");
        assertEquals(LocalDate.of(2025, 9, 5), s.getFechaInicio(), "La fecha de inicio debe ser 2025-09-05");
        assertEquals(LocalDate.of(2025, 9, 10), s.getFechaFin(), "La fecha de fin debe ser 2025-09-10");
        assertEquals(usuario, s.getUsuario(), "El usuario debe coincidir");
        assertEquals(disponibilidad, s.getDisponibilidad(), "La disponibilidad debe coincidir");
    }

    @Test
    void testCambioDeEstado() {
        SolicitudReserva s = new SolicitudReserva();
        s.setEstado("PENDIENTE");
        assertEquals("PENDIENTE", s.getEstado(), "El estado debe ser PENDIENTE");

        s.setEstado("ACEPTADA");
        assertEquals("ACEPTADA", s.getEstado(), "El estado debe haberse actualizado a ACEPTADA");

        s.setEstado("RECHAZADA");
        assertEquals("RECHAZADA", s.getEstado(), "El estado debe haberse actualizado a RECHAZADA");
    }

    @Test
    void testSetReserva() {
        SolicitudReserva s = new SolicitudReserva();
        Reserva reserva = new Reserva();
        s.setReserva(reserva);
        assertEquals(reserva, s.getReserva(), "La reserva asignada debe coincidir");
    }
}