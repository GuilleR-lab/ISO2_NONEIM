package com.example.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Usuario inquilino;
    private Inmueble inmueble;
    private Disponibilidad disponibilidad;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        Direccion dir = new Direccion("España", "Madrid", "28001", "Calle Real", "2", "1A");
        inquilino = new Usuario("inquilino1", "Marta", "Sanz", "marta@example.com", "pass", dir, Usuario.Rol.INQUILINO);
        Usuario propietario = new Usuario("prop1", "Carlos", "Pérez", "carlos@example.com", "pass", dir, Usuario.Rol.PROPIETARIO);
        inmueble = new Inmueble("Av. Libertad 10", "Sevilla", 60.0, Inmueble.Tipo.ESTUDIO, "Estudio moderno", propietario);
        disponibilidad = new Disponibilidad(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31), 60.0, true, inmueble);

        reserva = new Reserva();
        reserva.setFechaInicio(LocalDate.of(2025, 8, 5));
        reserva.setFechaFin(LocalDate.of(2025, 8, 12));
        reserva.setPagado(true);
        reserva.setActiva(true);
        reserva.setInquilino(inquilino);
        reserva.setInmueble(inmueble);
        reserva.setDisponibilidad(disponibilidad);
    }

    @Test
    void testGetters() {
        assertEquals(LocalDate.of(2025, 8, 5), reserva.getFechaInicio(), "La fecha de inicio debe ser 2025-08-05");
        assertEquals(LocalDate.of(2025, 8, 12), reserva.getFechaFin(), "La fecha de fin debe ser 2025-08-12");
        assertTrue(reserva.isPagado(), "La reserva debe estar pagada");
        assertTrue(reserva.isActiva(), "La reserva debe estar activa");
        assertEquals(inquilino, reserva.getInquilino(), "El inquilino debe coincidir");
        assertEquals(inmueble, reserva.getInmueble(), "El inmueble debe coincidir");
        assertEquals(disponibilidad, reserva.getDisponibilidad(), "La disponibilidad debe coincidir");
    }

    @Test
    void testSetActiva() {
        reserva.setActiva(false);
        assertFalse(reserva.isActiva(), "La reserva debe haberse desactivado");
    }

    @Test
    void testSetPagado() {
        reserva.setPagado(false);
        assertFalse(reserva.isPagado(), "La reserva debe haberse marcado como no pagada");
    }

    @Test
    void testSetId() {
        reserva.setIdReserva(5L);
        assertEquals(5L, reserva.getIdReserva(), "El id de la reserva debe ser 5");
    }

    @Test
    void testSetPago() {
        Pago pago = new Pago("PAYPAL", 420.0, reserva);
        reserva.setPago(pago);
        assertNotNull(reserva.getPago(), "El pago no debe ser nulo");
        assertEquals("PAYPAL", reserva.getPago().getMetodoPago(), "El método de pago debe ser PAYPAL");
    }

    @Test
    void testSetPoliticaCancelacion() {
        PoliticaCancelacion politica = new PoliticaCancelacion("Sin reembolso", 100.0);
        reserva.setPoliticaCancelacion(politica);
        assertEquals("Sin reembolso", reserva.getPoliticaCancelacion().getDescripcion(), "La descripción de la política debe ser Sin reembolso");
    }
}