package com.example.backend.service;

import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.model.Usuario;
import com.example.backend.model.Usuario.Rol;
import com.example.backend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void testShowAllUsuariosExistenUsuarios(){        //Arrange
        Usuario user1 = new Usuario();
        Usuario user2 = new Usuario();
        List <Usuario> users = List.of(user1, user2);

        when(usuarioRepository.findAll()).thenReturn(users);
        //Act
        List <Usuario> resultado = usuarioService.showAllUsuarios();
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(users.size(), resultado.size(), "verificacion");
        assertEquals(users.get(0), resultado.get(0), "verificacion");
        verify(usuarioRepository).findAll();

    }

    @Test
    void testShowAllUsuariosNoExistenUsuarios(){        //Arrange
        when(usuarioRepository.findAll()).thenReturn(emptyList());
        //Act
        List <Usuario> resultado = usuarioService.showAllUsuarios();
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(0, resultado.size(), "verificacion");
        verify(usuarioRepository).findAll();

    }

    @Test
    void testCreateUsuario(){
        //Arrange
        Usuario user = new Usuario();

        when(usuarioRepository.save(user)).thenReturn(user);
        //Act
        Usuario resultado = usuarioService.createUsuario(user);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(user, resultado, "verificacion");
        verify(usuarioRepository).save(user);
    }

    @Test
    void testUsuarioExistsExisteUsuarioPorEmail(){        //Arrange
        String email = "user@test.com";
        Usuario user = new Usuario();
        user.setEmail(email);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        //Act
        boolean resultado = usuarioService.usuarioExists(email);
        //Assert
        assertEquals(true, resultado, "verificacion");
        verify(usuarioRepository).existsByEmail(email);
    }

    @Test
    void testUsuarioExistsExisteUsuarioPorUsername(){        //Arrange
        String username = "testuser";
        Usuario user = new Usuario();
        user.setUsername(username);

        when(usuarioRepository.existsByUsername(username)).thenReturn(true);
        //Act
        boolean resultado = usuarioService.usuarioExists(username);
        //Assert
        assertEquals(true, resultado, "verificacion");
        verify(usuarioRepository).existsByUsername(username);
    }

    @Test
    void testUsuarioExistsNoExisteUsuario(){        //Arrange
        String username = "testuser";

        when(usuarioRepository.existsByUsername(username)).thenReturn(false);
        //Act
        boolean resultado = usuarioService.usuarioExists(username);

        //Assert
        assertEquals(false, resultado, "verificacion");
        verify(usuarioRepository).existsByUsername(username);
    }

    @Test
    void testFindByEmailOrUsernameExisteUsuarioPorEmailSinUsername(){        //Arrange
        String email = "user@test.com";
        Usuario user = new Usuario();
        user.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(user);
        //Act
        Optional<Usuario> resultado = usuarioService.findByEmailOrUsername(email);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(user, resultado.get(), "verificacion");
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void testFindByEmailOrUsernameExisteUsuarioPorUsernameSinEmail(){        //Arrange
        String username = "testuser";
        Usuario user = new Usuario();
        user.setUsername(username);

        when(usuarioRepository.findByUsername(username)).thenReturn(user);
        //Act
        Optional<Usuario> resultado = usuarioService.findByEmailOrUsername(username);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(user, resultado.get(), "verificacion");
        verify(usuarioRepository).findByUsername(username);
    }

    @Test
    void testFindByEmailOrUsernameNoexisteUsuario(){        //Arrange
        String identifier = "testuser";

        when(usuarioRepository.findByUsername(identifier)).thenReturn(null);
        //Act y Assert
        Optional <Usuario> resultado = usuarioService.findByEmailOrUsername(identifier);

        assertNotNull(resultado, "verificacion");
        assertEquals(Optional.empty(), resultado, "verificacion");
        verify(usuarioRepository).findByUsername(identifier);
    }

    @Test
    void testFindByIdExisteUsuario(){        //Arrange
        Long id = 1L;
        Usuario user = new Usuario();
        user.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(user));
        //Act
        Optional <Usuario> resultado = usuarioService.findById(id);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(user.getId(), resultado.get().getId(), "verificacion");
        verify(usuarioRepository).findById(id);
    }

    @Test
    void testFindByIdNoExisteUsuario(){        //Arrange
        Long id = 1L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        //Act
        Optional <Usuario> resultado = usuarioService.findById(id);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals(Optional.empty(), resultado, "verificacion");
        verify(usuarioRepository).findById(id);

    }

    @Test
    void testUpdateUsuarioExisteUsuarioRolNulo(){        // Arrange
        Long id = 1L;
        Rol rol = Usuario.Rol.INQUILINO;
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setId(id);
        usuarioOriginal.setUsername("viejo");
        usuarioOriginal.setRol(rol);

        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setUsername("nuevo");
        usuarioNuevo.setEmail("nuevo@mail.com");
        usuarioNuevo.setRol(null); // Queremos probar que NO sobrescribe el rol original

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioOriginal));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Usuario resultado = usuarioService.updateUsuario(usuarioNuevo, id);

        // Assert
        assertNotNull(resultado, "verificacion");
        assertEquals("nuevo", resultado.getUsername(), "verificacion");
        assertEquals("nuevo@mail.com", resultado.getEmail(), "verificacion");
        assertEquals(Usuario.Rol.INQUILINO, resultado.getRol(), "El rol debería mantenerse si el nuevo es null");

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuarioExisteUsuarioRolNoNulo(){        //Arrange
        Long id = 1L;
        Rol rol = Usuario.Rol.INQUILINO;
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setId(id);
        usuarioOriginal.setEmail("user@test.com");
        usuarioOriginal.setRol(rol);

        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setEmail("nuevo@test.com");
        usuarioNuevo.setRol(Usuario.Rol.PROPIETARIO);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioOriginal));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        //Act
        Usuario resultado = usuarioService.updateUsuario(usuarioNuevo, id);
        //Assert
        assertNotNull(resultado, "verificacion");
        assertEquals("nuevo@test.com", resultado.getEmail(), "verificacion");
        assertEquals(Usuario.Rol.PROPIETARIO, resultado.getRol(), "verificacion");

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuarioNoExisteUsuario(){        //Arrange
        Long id = 1L;
        Usuario nuevoUsuario = new Usuario();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        //Act
        Usuario resultado = usuarioService.updateUsuario(nuevoUsuario, id);

        //Assert
        assertNull(resultado, "verificacion");
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testDeleteUsuarioExisteUsuario(){        //Arrange
        Long id = 1L;

        when(usuarioRepository.existsById(id)).thenReturn(true);
        //Act
        usuarioService.deleteUsuario(id);
        //Assert
        verify(usuarioRepository).existsById(id);
        verify(usuarioRepository).deleteById(id);

        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void testDeleteUsuarioNoExisteUsuario(){        //Arrange
        Long id = 1L;

        when(usuarioRepository.existsById(id)).thenReturn(false);
        //Act y Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.deleteUsuario(id);
        }, "verificacion");
        assertEquals("Usuario no encontrado", exception.getMessage(), "verificacion");
        verify(usuarioRepository).existsById(id);
        verify(usuarioRepository, never()).deleteById(id);
    }
}