package com.example.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.backend.model.Usuario;
import com.example.backend.service.SessionService;

public class SessionServiceTest {
    
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
    }

    @Test
    void storeSession_GuardadoExitoso_Test() {
        //Arrange
        String token = "1234";
        Usuario usuario = new Usuario();
        usuario.setId(1L);   
        //Act
        sessionService.storeSession(token, usuario);
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNotNull(usuarioObtenido);
        assertEquals(usuario.getId(), usuarioObtenido.getId());
    }

    @Test
    void getUsuarioFromToken_ExisteToken_Test(){
        //Arrange
        String token = "1234";
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        sessionService.storeSession(token, usuario);
        //Act
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNotNull(usuarioObtenido);
        assertEquals(usuario.getId(), usuarioObtenido.getId());
    }

    @Test
    void getUsuarioFromToken_NoExisteToken_Test(){
        //Arrange
        String token = "1234";
        //Act
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNull(usuarioObtenido);
    }

    @Test
    void deleteSession_ExisteSesion_Test(){
        //Arrange
        String token = "1234";
        Usuario usuario = new Usuario();

        sessionService.storeSession(token, usuario);
        //Act
        sessionService.deleteSession(token);
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNull(usuarioObtenido);
    }

    @Test
    void deleteSession_NoExisteSesion_Test(){
        //Arrange
        String token = "1234";
        //Act y Assert
        sessionService.deleteSession(token);
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        assertNull(usuarioObtenido);
    }

    @Test
    void deleteSession_NoDebeAfectarAOtrasSesiones_Test() {
        //Arrange
        sessionService.storeSession("token1", new Usuario());
        sessionService.storeSession("token2", new Usuario());
        //Act
        sessionService.deleteSession("token1");
        //Assert
        assertNull(sessionService.getUsuarioFromToken("token1"));
        assertNotNull(sessionService.getUsuarioFromToken("token2"));
    }
}
