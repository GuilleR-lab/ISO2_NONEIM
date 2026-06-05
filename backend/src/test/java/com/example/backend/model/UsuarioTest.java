package com.example.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Direccion direccion;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        direccion = new Direccion("España", "Madrid", "28001", "Calle Mayor", "1", "2A");
        usuario = new Usuario("juanito", "Juan", "García", "juan@example.com", "pass123", direccion, Usuario.Rol.INQUILINO);
    }

    @Test
    void testConstructorYGetters() {
        assertEquals("juanito", usuario.getUsername(), "El username debe ser juanito");
        assertEquals("Juan", usuario.getName(), "El nombre debe ser Juan");
        assertEquals("García", usuario.getSurname(), "El apellido debe ser García");
        assertEquals("juan@example.com", usuario.getEmail(), "El email debe ser juan@example.com");
        assertEquals("pass123", usuario.getPassword(), "La contraseña debe ser pass123");
        assertEquals(Usuario.Rol.INQUILINO, usuario.getRol(), "El rol debe ser INQUILINO");
        assertNotNull(usuario.getAddress(), "La dirección no debe ser nula");
    }

    @Test
    void testSetters() {
        usuario.setUsername("nuevo_user");
        usuario.setName("Pedro");
        usuario.setSurname("López");
        usuario.setEmail("pedro@example.com");
        usuario.setPassword("newpass");
        usuario.setRol(Usuario.Rol.PROPIETARIO);

        assertEquals("nuevo_user", usuario.getUsername(), "El username debe haberse actualizado");
        assertEquals("Pedro", usuario.getName(), "El nombre debe haberse actualizado");
        assertEquals("López", usuario.getSurname(), "El apellido debe haberse actualizado");
        assertEquals("pedro@example.com", usuario.getEmail(), "El email debe haberse actualizado");
        assertEquals("newpass", usuario.getPassword(), "La contraseña debe haberse actualizado");
        assertEquals(Usuario.Rol.PROPIETARIO, usuario.getRol(), "El rol debe haberse actualizado a PROPIETARIO");
    }

    @Test
    void testConstructorVacio() {
        Usuario u = new Usuario();
        assertNull(u.getUsername(), "El username debe ser nulo en constructor vacío");
        assertNull(u.getEmail(), "El email debe ser nulo en constructor vacío");
    }

    @Test
    void testSetAddress() {
        Direccion nuevaDireccion = new Direccion("Francia", "París", "75001", "Champs-Élysées", "10", "3B");
        usuario.setAddress(nuevaDireccion);
        assertEquals("Francia", usuario.getAddress().getPais(), "El país de la nueva dirección debe ser Francia");
    }

    @Test
    void testRolValues() {
        assertEquals(2, Usuario.Rol.values().length, "Debe haber exactamente 2 roles");
        assertEquals(Usuario.Rol.INQUILINO, Usuario.Rol.valueOf("INQUILINO"), "Debe existir el rol INQUILINO");
        assertEquals(Usuario.Rol.PROPIETARIO, Usuario.Rol.valueOf("PROPIETARIO"), "Debe existir el rol PROPIETARIO");
    }

    @Test
    void testSetId() {
        usuario.setId(42L);
        assertEquals(42L, usuario.getId(), "El id debe ser 42");
    }
}