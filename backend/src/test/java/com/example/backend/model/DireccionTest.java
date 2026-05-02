package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void testConstructorYGetters() {
        Direccion d = new Direccion("España", "Valencia", "46001", "Gran Vía", "5", "1C");

        assertEquals("España", d.getPais());
        assertEquals("Valencia", d.getCiudad());
        assertEquals("46001", d.getCodigoPostal());
        assertEquals("Gran Vía", d.getCalle());
        assertEquals("5", d.getEdificio());
        assertEquals("1C", d.getPiso());
    }

    @Test
    void testConstructorVacio() {
        Direccion d = new Direccion();
        assertNull(d.getPais());
        assertNull(d.getCiudad());
    }

    @Test
    void testSetters() {
        Direccion d = new Direccion();
        d.setPais("Portugal");
        d.setCiudad("Lisboa");
        d.setCodigoPostal("1000-001");
        d.setCalle("Avenida da Liberdade");
        d.setEdificio("3");
        d.setPiso("2D");

        assertEquals("Portugal", d.getPais());
        assertEquals("Lisboa", d.getCiudad());
        assertEquals("1000-001", d.getCodigoPostal());
        assertEquals("Avenida da Liberdade", d.getCalle());
        assertEquals("3", d.getEdificio());
        assertEquals("2D", d.getPiso());
    }

    @Test
    void testPisoNullable() {
        Direccion d = new Direccion("España", "Sevilla", "41001", "Calle Real", "2", null);
        assertNull(d.getPiso());
    }
}
