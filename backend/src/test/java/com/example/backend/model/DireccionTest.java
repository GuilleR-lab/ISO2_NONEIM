package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void testConstructorYGetters() {
        Direccion d = new Direccion("España", "Valencia", "46001", "Gran Vía", "5", "1C");

        assertEquals("España", d.getPais(), "El país debe ser España");
        assertEquals("Valencia", d.getCiudad(), "La ciudad debe ser Valencia");
        assertEquals("46001", d.getCodigoPostal(), "El código postal debe ser 46001");
        assertEquals("Gran Vía", d.getCalle(), "La calle debe ser Gran Vía");
        assertEquals("5", d.getEdificio(), "El edificio debe ser 5");
        assertEquals("1C", d.getPiso(), "El piso debe ser 1C");
    }

    @Test
    void testConstructorVacio() {
        Direccion d = new Direccion();
        assertNull(d.getPais(), "El país debe ser nulo en constructor vacío");
        assertNull(d.getCiudad(), "La ciudad debe ser nula en constructor vacío");
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

        assertEquals("Portugal", d.getPais(), "El país debe haberse actualizado a Portugal");
        assertEquals("Lisboa", d.getCiudad(), "La ciudad debe haberse actualizado a Lisboa");
        assertEquals("1000-001", d.getCodigoPostal(), "El código postal debe haberse actualizado");
        assertEquals("Avenida da Liberdade", d.getCalle(), "La calle debe haberse actualizado");
        assertEquals("3", d.getEdificio(), "El edificio debe haberse actualizado a 3");
        assertEquals("2D", d.getPiso(), "El piso debe haberse actualizado a 2D");
    }

    @Test
    void testPisoNullable() {
        Direccion d = new Direccion("España", "Sevilla", "41001", "Calle Real", "2", null);
        assertNull(d.getPiso(), "El piso debe admitir valores nulos");
    }
}