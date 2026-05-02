package com.example.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadTest {

    private Inmueble inmueble;
    private Disponibilidad disponibilidad;

    @BeforeEach
    void setUp() {
        Direccion dir = new Direccion("España", "Madrid", "28001", "Gran Vía", "1", null);
        Usuario propietario = new Usuario("prop1", "Carlos", "Pérez", "carlos@example.com", "pass", dir, Usuario.Rol.PROPIETARIO);
        inmueble = new Inmueble("Calle Mayor 5", "Madrid", 75.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Casa céntrica", propietario);

        disponibilidad = new Disponibilidad(
            LocalDate.of(2025, 6, 1),
            LocalDate.of(2025, 6, 30),
            75.0,
            true,
            inmueble
        );
    }

    @Test
    void testConstructorYGetters() {
        assertEquals(LocalDate.of(2025, 6, 1), disponibilidad.getFechaInicio());
        assertEquals(LocalDate.of(2025, 6, 30), disponibilidad.getFechaFin());
        assertEquals(75.0, disponibilidad.getPrecio());
        assertTrue(disponibilidad.isDirecta());
        assertEquals(inmueble, disponibilidad.getInmueble());
    }

    @Test
    void testConstructorVacio() {
        Disponibilidad d = new Disponibilidad();
        assertNull(d.getFechaInicio());
        assertNull(d.getFechaFin());
        assertFalse(d.isDirecta());
    }

    @Test
    void testSetters() {
        disponibilidad.setFechaInicio(LocalDate.of(2025, 7, 1));
        disponibilidad.setFechaFin(LocalDate.of(2025, 7, 31));
        disponibilidad.setPrecio(95.0);
        disponibilidad.setDirecta(false);

        assertEquals(LocalDate.of(2025, 7, 1), disponibilidad.getFechaInicio());
        assertEquals(LocalDate.of(2025, 7, 31), disponibilidad.getFechaFin());
        assertEquals(95.0, disponibilidad.getPrecio());
        assertFalse(disponibilidad.isDirecta());
    }

    @Test
    void testSetId() {
        disponibilidad.setIdDisponibilidad(10L);
        assertEquals(10L, disponibilidad.getIdDisponibilidad());
    }

    @Test
    void testSetInmueble() {
        Disponibilidad d = new Disponibilidad();
        d.setInmueble(inmueble);
        assertEquals(inmueble, d.getInmueble());
    }
}
