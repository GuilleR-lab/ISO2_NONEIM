package com.example.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ListaDeseosTest {

    private Usuario usuario;
    private Inmueble inmueble1;
    private Inmueble inmueble2;

    @BeforeEach
    void setUp() {
        Direccion dir = new Direccion("España", "Valencia", "46001", "C/ Colón", "5", "3B");
        usuario = new Usuario("user1", "Sara", "Gil", "sara@example.com", "pass", dir, Usuario.Rol.INQUILINO);
        Usuario prop = new Usuario("prop1", "Miguel", "Ros", "miguel@example.com", "pass", dir, Usuario.Rol.PROPIETARIO);
        inmueble1 = new Inmueble("C/ Paz 1", "Valencia", 70.0, Inmueble.Tipo.APARTAMENTO, "Moderno", prop);
        inmueble2 = new Inmueble("C/ Luna 5", "Valencia", 55.0, Inmueble.Tipo.HABITACION, "Tranquilo", prop);
    }

    @Test
    void testConstructorConUsuario() {
        ListaDeseos lista = new ListaDeseos(usuario);
        assertEquals(usuario, lista.getUsuario());
        assertNotNull(lista.getInmuebles());
        assertTrue(lista.getInmuebles().isEmpty());
    }

    @Test
    void testConstructorVacio() {
        ListaDeseos lista = new ListaDeseos();
        assertNull(lista.getUsuario());
    }

    @Test
    void testAgregarInmuebles() {
        ListaDeseos lista = new ListaDeseos(usuario);
        Set<Inmueble> inmuebles = new HashSet<>();
        inmuebles.add(inmueble1);
        inmuebles.add(inmueble2);
        lista.setInmuebles(inmuebles);

        assertEquals(2, lista.getInmuebles().size());
        assertTrue(lista.getInmuebles().contains(inmueble1));
        assertTrue(lista.getInmuebles().contains(inmueble2));
    }

    @Test
    void testGetIdListaInicialmenteNull() {
        ListaDeseos lista = new ListaDeseos(usuario);
        assertNull(lista.getIdLista());
    }

    @Test
    void testSetUsuario() {
        ListaDeseos lista = new ListaDeseos();
        lista.setUsuario(usuario);
        assertEquals(usuario, lista.getUsuario());
    }
}
