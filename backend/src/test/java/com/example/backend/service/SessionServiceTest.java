package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.backend.model.Usuario;

public class SessionServiceTest {
    
    private SessionService sessionService;

    @BeforeEach
    void testSetUp(){
        sessionService = new SessionService();
    }

    @Test
    void testStoreSession_GuardadoExitoso(){
        //Arrange
        String token = "1234";
        Usuario usuario = new Usuario();
        usuario.setId(1L);   
        //Act
        sessionService.storeSession(token, usuario);
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNotNull(usuarioObtenido, "verificacion");
        assertEquals(usuario.getId(), usuarioObtenido.getId(), "verificacion");
    }

    @Test
    void testGetUsuarioFromToken_ExisteToken(){
        //Arrange
        String token = "1234";
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        sessionService.storeSession(token, usuario);
        //Act
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNotNull(usuarioObtenido, "verificacion");
        assertEquals(usuario.getId(), usuarioObtenido.getId(), "verificacion");
    }

    @Test
    void testGetUsuarioFromToken_NoExisteToken(){
        //Arrange
        String token = "1234";
        //Act
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNull(usuarioObtenido, "verificacion");
    }

    @Test
    void testDeleteSession_ExisteSesion(){
        //Arrange
        String token = "1234";
        Usuario usuario = new Usuario();

        sessionService.storeSession(token, usuario);
        //Act
        sessionService.deleteSession(token);
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        //Assert
        assertNull(usuarioObtenido, "verificacion");
    }

    @Test
    void testDeleteSession_NoExisteSesion(){
        //Arrange
        String token = "1234";
        //Act y Assert
        sessionService.deleteSession(token);
        Usuario usuarioObtenido = sessionService.getUsuarioFromToken(token);
        assertNull(usuarioObtenido, "verificacion");
    }

    @Test
    void testDeleteSession_NoDebeAfectarAOtrasSesiones(){
        //Arrange
        sessionService.storeSession("token1", new Usuario());
        sessionService.storeSession("token2", new Usuario());
        //Act
        sessionService.deleteSession("token1");
        //Assert
        assertNull(sessionService.getUsuarioFromToken("token1"), "verificacion");
        assertNotNull(sessionService.getUsuarioFromToken("token2"), "verificacion");
    }
}