package com.example.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InmuebleTest {

    private Usuario propietario;
    private Inmueble inmueble;

    @BeforeEach
    void setUp() {
        Direccion dir = new Direccion("España", "Barcelona", "08001", "Paseo de Gracia", "1", null);
        propietario = new Usuario("propietario1", "Ana", "Martínez", "ana@example.com", "pass", dir, Usuario.Rol.PROPIETARIO);
        inmueble = new Inmueble("Calle Falsa 123", "Madrid", 85.0, Inmueble.Tipo.APARTAMENTO, "Piso céntrico", propietario);
    }

    @Test
    void testConstructorYGetters() {
        assertEquals("Calle Falsa 123", inmueble.getDireccion(), "La dirección debe ser Calle Falsa 123");
        assertEquals("Madrid", inmueble.getCiudad(), "La ciudad debe ser Madrid");
        assertEquals(85.0, inmueble.getPrecioNoche(), "El precio por noche debe ser 85.0");
        assertEquals(Inmueble.Tipo.APARTAMENTO, inmueble.getTipo(), "El tipo debe ser APARTAMENTO");
        assertEquals("Piso céntrico", inmueble.getDescripcion(), "La descripción debe ser Piso céntrico");
        assertEquals(propietario, inmueble.getPropietario(), "El propietario debe coincidir");
    }

    @Test
    void testConstructorVacio() {
        Inmueble i = new Inmueble();
        assertNull(i.getDireccion(), "La dirección debe ser nula en constructor vacío");
        assertNull(i.getCiudad(), "La ciudad debe ser nula en constructor vacío");
    }

    @Test
    void testSetters() {
        inmueble.setDireccion("Av. España 10");
        inmueble.setCiudad("Zaragoza");
        inmueble.setPrecioNoche(120.0);
        inmueble.setTipo(Inmueble.Tipo.HABITACION);
        inmueble.setDescripcion("Habitación luminosa");

        assertEquals("Av. España 10", inmueble.getDireccion(), "La dirección debe haberse actualizado");
        assertEquals("Zaragoza", inmueble.getCiudad(), "La ciudad debe haberse actualizado a Zaragoza");
        assertEquals(120.0, inmueble.getPrecioNoche(), "El precio debe haberse actualizado a 120.0");
        assertEquals(Inmueble.Tipo.HABITACION, inmueble.getTipo(), "El tipo debe haberse actualizado a HABITACION");
        assertEquals("Habitación luminosa", inmueble.getDescripcion(), "La descripción debe haberse actualizado");
    }

    @Test
    void testTipoValues() {
        assertEquals(4, Inmueble.Tipo.values().length, "Debe haber exactamente 4 tipos de inmueble");
        assertNotNull(Inmueble.Tipo.valueOf("VIVIENDA_COMPLETA"), "Debe existir el tipo VIVIENDA_COMPLETA");
        assertNotNull(Inmueble.Tipo.valueOf("HABITACION"), "Debe existir el tipo HABITACION");
        assertNotNull(Inmueble.Tipo.valueOf("ESTUDIO"), "Debe existir el tipo ESTUDIO");
        assertNotNull(Inmueble.Tipo.valueOf("APARTAMENTO"), "Debe existir el tipo APARTAMENTO");
    }

    @Test
    void testSetPropietario() {
        Direccion dir2 = new Direccion("España", "Málaga", "29001", "Calle Larios", "3", "1A");
        Usuario otroProp = new Usuario("prop2", "Luis", "Ruiz", "luis@example.com", "1234", dir2, Usuario.Rol.PROPIETARIO);
        inmueble.setPropietario(otroProp);
        assertEquals("Luis", inmueble.getPropietario().getName(), "El nombre del nuevo propietario debe ser Luis");
    }

    @Test
    void testSetId() {
        inmueble.setIdInmueble(99L);
        assertEquals(99L, inmueble.getIdInmueble(), "El id del inmueble debe ser 99");
    }
}