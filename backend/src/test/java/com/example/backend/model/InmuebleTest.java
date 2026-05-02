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
        assertEquals("Calle Falsa 123", inmueble.getDireccion());
        assertEquals("Madrid", inmueble.getCiudad());
        assertEquals(85.0, inmueble.getPrecioNoche());
        assertEquals(Inmueble.Tipo.APARTAMENTO, inmueble.getTipo());
        assertEquals("Piso céntrico", inmueble.getDescripcion());
        assertEquals(propietario, inmueble.getPropietario());
    }

    @Test
    void testConstructorVacio() {
        Inmueble i = new Inmueble();
        assertNull(i.getDireccion());
        assertNull(i.getCiudad());
    }

    @Test
    void testSetters() {
        inmueble.setDireccion("Av. España 10");
        inmueble.setCiudad("Zaragoza");
        inmueble.setPrecioNoche(120.0);
        inmueble.setTipo(Inmueble.Tipo.HABITACION);
        inmueble.setDescripcion("Habitación luminosa");

        assertEquals("Av. España 10", inmueble.getDireccion());
        assertEquals("Zaragoza", inmueble.getCiudad());
        assertEquals(120.0, inmueble.getPrecioNoche());
        assertEquals(Inmueble.Tipo.HABITACION, inmueble.getTipo());
        assertEquals("Habitación luminosa", inmueble.getDescripcion());
    }

    @Test
    void testTipoValues() {
        assertEquals(4, Inmueble.Tipo.values().length);
        assertNotNull(Inmueble.Tipo.valueOf("VIVIENDA_COMPLETA"));
        assertNotNull(Inmueble.Tipo.valueOf("HABITACION"));
        assertNotNull(Inmueble.Tipo.valueOf("ESTUDIO"));
        assertNotNull(Inmueble.Tipo.valueOf("APARTAMENTO"));
    }

    @Test
    void testSetPropietario() {
        Direccion dir2 = new Direccion("España", "Málaga", "29001", "Calle Larios", "3", "1A");
        Usuario otroProp = new Usuario("prop2", "Luis", "Ruiz", "luis@example.com", "1234", dir2, Usuario.Rol.PROPIETARIO);
        inmueble.setPropietario(otroProp);
        assertEquals("Luis", inmueble.getPropietario().getName());
    }

    @Test
    void testSetId() {
        inmueble.setIdInmueble(99L);
        assertEquals(99L, inmueble.getIdInmueble());
    }
}
