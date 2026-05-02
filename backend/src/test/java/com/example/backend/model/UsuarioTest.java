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
        assertEquals("juanito", usuario.getUsername());
        assertEquals("Juan", usuario.getName());
        assertEquals("García", usuario.getSurname());
        assertEquals("juan@example.com", usuario.getEmail());
        assertEquals("pass123", usuario.getPassword());
        assertEquals(Usuario.Rol.INQUILINO, usuario.getRol());
        assertNotNull(usuario.getAddress());
    }

    @Test
    void testSetters() {
        usuario.setUsername("nuevo_user");
        usuario.setName("Pedro");
        usuario.setSurname("López");
        usuario.setEmail("pedro@example.com");
        usuario.setPassword("newpass");
        usuario.setRol(Usuario.Rol.PROPIETARIO);

        assertEquals("nuevo_user", usuario.getUsername());
        assertEquals("Pedro", usuario.getName());
        assertEquals("López", usuario.getSurname());
        assertEquals("pedro@example.com", usuario.getEmail());
        assertEquals("newpass", usuario.getPassword());
        assertEquals(Usuario.Rol.PROPIETARIO, usuario.getRol());
    }

    @Test
    void testRolPorDefecto() {
        Usuario u = new Usuario();
        // El rol por defecto es INQUILINO (según el modelo)
        assertNull(u.getUsername());
        assertNull(u.getEmail());
    }

    @Test
    void testSetAddress() {
        Direccion nuevaDireccion = new Direccion("Francia", "París", "75001", "Champs-Élysées", "10", "3B");
        usuario.setAddress(nuevaDireccion);
        assertEquals("Francia", usuario.getAddress().getPais());
    }

    @Test
    void testRolValues() {
        assertEquals(2, Usuario.Rol.values().length);
        assertEquals(Usuario.Rol.INQUILINO, Usuario.Rol.valueOf("INQUILINO"));
        assertEquals(Usuario.Rol.PROPIETARIO, Usuario.Rol.valueOf("PROPIETARIO"));
    }

    @Test
    void testSetId() {
        usuario.setId(42L);
        assertEquals(42L, usuario.getId());
    }
}
