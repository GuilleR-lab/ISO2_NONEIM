package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoliticaCancelacionTest {

    @Test
    void testConstructorYGetters() {
        PoliticaCancelacion p = new PoliticaCancelacion("Reembolso al 50%", 50.0);
        assertEquals("Reembolso al 50%", p.getDescripcion());
        assertEquals(50.0, p.getPenalizacion());
    }

    @Test
    void testConstructorVacio() {
        PoliticaCancelacion p = new PoliticaCancelacion();
        assertNull(p.getDescripcion());
        assertEquals(0.0, p.getPenalizacion());
    }

    @Test
    void testSetters() {
        PoliticaCancelacion p = new PoliticaCancelacion();
        p.setDescripcion("Sin reembolso");
        p.setPenalizacion(100.0);
        p.setIdPolitica(3L);

        assertEquals("Sin reembolso", p.getDescripcion());
        assertEquals(100.0, p.getPenalizacion());
        assertEquals(3L, p.getIdPolitica());
    }

    @Test
    void testReservasInicialesVacías() {
        PoliticaCancelacion p = new PoliticaCancelacion("Flexible", 0.0);
        assertNotNull(p.getReservas());
        assertTrue(p.getReservas().isEmpty());
    }

    @Test
    void testSetReservas() {
        PoliticaCancelacion p = new PoliticaCancelacion("Moderada", 25.0);
        java.util.Set<Reserva> reservas = new java.util.HashSet<>();
        reservas.add(new Reserva());
        p.setReservas(reservas);
        assertEquals(1, p.getReservas().size());
    }
}