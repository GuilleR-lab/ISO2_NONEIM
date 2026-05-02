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
        assertEquals(LocalDate.of(2025, 8, 5), reserva.getFechaInicio());
        assertEquals(LocalDate.of(2025, 8, 12), reserva.getFechaFin());
        assertTrue(reserva.isPagado());
        assertTrue(reserva.isActiva());
        assertEquals(inquilino, reserva.getInquilino());
        assertEquals(inmueble, reserva.getInmueble());
        assertEquals(disponibilidad, reserva.getDisponibilidad());
    }

    @Test
    void testSetters() {
        reserva.setActiva(false);
        reserva.setPagado(false);
        assertFalse(reserva.isActiva());
        assertFalse(reserva.isPagado());
    }

    @Test
    void testSetId() {
        reserva.setIdReserva(5L);
        assertEquals(5L, reserva.getIdReserva());
    }

    @Test
    void testSetPago() {
        Pago pago = new Pago("PAYPAL", 420.0, reserva);
        reserva.setPago(pago);
        assertNotNull(reserva.getPago());
        assertEquals("PAYPAL", reserva.getPago().getMetodoPago());
    }

    @Test
    void testSetPoliticaCancelacion() {
        PoliticaCancelacion politica = new PoliticaCancelacion("Sin reembolso", 100.0);
        reserva.setPoliticaCancelacion(politica);
        assertEquals("Sin reembolso", reserva.getPoliticaCancelacion().getDescripcion());
    }
}
