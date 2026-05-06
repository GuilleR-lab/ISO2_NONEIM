package com.example.backend.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PoliticaCancelacionTest {

    @Test
    void testConstructorYGetters() {
        PoliticaCancelacion p = new PoliticaCancelacion("Reembolso al 50%", 50.0);
        assertEquals("Reembolso al 50%", p.getDescripcion(), "La descripción debe ser Reembolso al 50%");
        assertEquals(50.0, p.getPenalizacion(), "La penalización debe ser 50.0");
    }

    @Test
    void testConstructorVacio() {
        PoliticaCancelacion p = new PoliticaCancelacion();
        assertNull(p.getDescripcion(), "La descripción debe ser nula en constructor vacío");
        assertEquals(0.0, p.getPenalizacion(), "La penalización debe ser 0.0 en constructor vacío");
    }

    @Test
    void testSetters() {
        PoliticaCancelacion p = new PoliticaCancelacion();
        p.setDescripcion("Sin reembolso");
        p.setPenalizacion(100.0);
        p.setIdPolitica(3L);

        assertEquals("Sin reembolso", p.getDescripcion(), "La descripción debe haberse actualizado a Sin reembolso");
        assertEquals(100.0, p.getPenalizacion(), "La penalización debe haberse actualizado a 100.0");
        assertEquals(3L, p.getIdPolitica(), "El id de la política debe ser 3");
    }

    @Test
    void testReservasInicialesVacias() {
        PoliticaCancelacion p = new PoliticaCancelacion("Flexible", 0.0);
        assertNotNull(p.getReservas(), "La lista de reservas no debe ser nula");
        assertTrue(p.getReservas().isEmpty(), "La lista de reservas debe estar vacía inicialmente");
    }

    @Test
    void testSetReservas() {
        PoliticaCancelacion p = new PoliticaCancelacion("Moderada", 25.0);
        Set<Reserva> reservas = new HashSet<>();
        reservas.add(new Reserva());
        p.setReservas(reservas);
        assertEquals(1, p.getReservas().size(), "La lista de reservas debe tener 1 elemento");
        assertFalse(p.getReservas().isEmpty(), "La lista de reservas no debe estar vacía");
    }
}