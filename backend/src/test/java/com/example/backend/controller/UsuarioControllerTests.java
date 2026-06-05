package com.example.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.backend.dto.AuthDTO.LoginRequest;
import com.example.backend.dto.AuthDTO.RegisterRequest;
import com.example.backend.model.Direccion;
import com.example.backend.model.Usuario;
import com.example.backend.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginIdNull() throws Exception{
        //Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail(null);

        when(usuarioService.findByEmailOrUsername(anyString())).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Faltan campos obligatorios"));
    }

    @Test
    public void testLoginNoExisteUsuario() throws Exception{
        //Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("password");

        when(usuarioService.findByEmailOrUsername(anyString())).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    public void testLoginContrasenaIncorrecta() throws Exception {        //Arrange
        Usuario user = new Usuario();
        user.setPassword("claveGuardada");

        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("claveIncorrecta");

        when(usuarioService.findByEmailOrUsername(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Contraseña incorrecta"));
    }

    @Test
    public void testLoginOk() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setPassword("password");

        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("password");

        when(usuarioService.findByEmailOrUsername("user@test.com")).thenReturn(Optional.of(user));
        //Act & Assert
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Inicio de sesion correcto"));
    }

    @Test
    public void testRegisterYaExiste() throws Exception{
        //Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@test.com");

        when(usuarioService.usuarioExists(anyString())).thenReturn(true);
        //Act & Assert
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("Error usuario ya registrado"));
    }

    @Test
    public void testRegisterFaltanCampos() throws Exception{
        //Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@test.com");

        when(usuarioService.usuarioExists(anyString())).thenReturn(false);
        //Act & Assert
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Faltan campos obligatorios"));
    }

    @Test
    public void testRegisterOk() throws Exception{
        //Arrange
        Usuario nuevo = new Usuario();
        Direccion address = new Direccion();
        address.setPais("España");
        address.setCiudad("Madrid");
        address.setCodigoPostal("28001");
        address.setCalle("Gran Vía");
        address.setEdificio("1A");

        RegisterRequest request = new RegisterRequest();
        request.setAddress(address);
        request.setEmail("user@test.com");
        request.setName("Juan"); 
        request.setSurname("Gonzalez");
        request.setUsername("testerJG");
        request.setPassword("password");
        request.setRol("INQUILINO");

        when(usuarioService.usuarioExists(anyString())).thenReturn(false);
        when(usuarioService.createUsuario(any(Usuario.class))).thenReturn(nuevo);
        //Act & Assert
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Usuario registrado correctamente"));
    }

    @Test
    public void testCambiarRolUsuarioNoExiste() throws Exception {
        //Arrange
        when(usuarioService.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/rol", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("rol", "PROPIETARIO"))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    public void testCambiarRolOk() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        Usuario.Rol INQUILINO = Usuario.Rol.INQUILINO;
        Usuario.Rol nuevoRol = Usuario.Rol.PROPIETARIO;

        user.setId(1L);
        user.setRol(INQUILINO);

        when(usuarioService.findById(1L)).thenReturn(Optional.of(user));
        when(usuarioService.updateUsuario(any(Usuario.class), eq(1L))).thenReturn(user);
        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/rol", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("rol", nuevoRol.name()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Rol actualizado correctamente"));
    }

    @Test
    public void testCambiarRolRolNoValido() throws Exception {
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        user.setRol(Usuario.Rol.INQUILINO);

        when(usuarioService.findById(1L)).thenReturn(Optional.of(user));

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/rol", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("rol", "ROL_INVALIDO"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Rol no válido"));
    }

    @Test
    public void testFindByIdUsuarioNoExiste() throws Exception {
        //Arrange
        when(usuarioService.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(get("/api/auth/{id}", 1L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    public void testFindByIdOk() throws Exception {
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        user.setUsername("testerJG");
        user.setName("Juan");
        user.setSurname("Gonzalez");
        user.setEmail("user@test.com");

        when(usuarioService.findById(1L)).thenReturn(Optional.of(user));

        //Act & Assert
        mockMvc.perform(get("/api/auth/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value("testerJG"))
            .andExpect(jsonPath("$.name").value("Juan"))
            .andExpect(jsonPath("$.surname").value("Gonzalez"))
            .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @Test
    public void testCambiarPasswordContrasenaVacia() throws Exception{        //Arrange
        Map<String, String> body = Map.of(
            "passwordActual", "",
            "passwordNueva", "newPassword"
        );

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/password", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Faltan campos obligatorios"));
    }

    @Test
    public void testCambiarPasswordUsuarioNoExiste() throws Exception{
        //Arrange
        Map<String, String> body = Map.of(
            "passwordActual", "oldPassword",
            "passwordNueva", "newPassword"
        );

        when(usuarioService.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/password", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    public void testCambiarPasswordContrasenaActualIncorrecta() throws Exception{        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        user.setPassword("oldPassword");

        Map<String, String> body = Map.of(
            "passwordActual", "incorrectPassword",
            "passwordNueva", "newPassword"
        );

        when(usuarioService.findById(1L)).thenReturn(Optional.of(user));

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/password", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("La contraseña actual no es correcta"));
    }

    @Test
    public void testCambiarPasswordOk() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);
        user.setPassword("oldPassword");

        Map<String, String> body = Map.of(
            "passwordActual", "oldPassword",
            "passwordNueva", "newPassword"
        );

        when(usuarioService.findById(1L)).thenReturn(Optional.of(user));
        when(usuarioService.updateUsuario(any(Usuario.class), eq(1L))).thenReturn(user);

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/password", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Contraseña actualizada correctamente"));
    }

    @Test
    public void testEditarDireccionFaltanCampos() throws Exception{
        //Arrange
        Direccion nueva = new Direccion();
        nueva.setPais("España");
        nueva.setCiudad("Madrid");

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/direccion", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nueva)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Faltan campos obligatorios de la dirección"));
    }

    @Test
    public void testEditarDireccionUsuarioNoExiste() throws Exception{
        //Arrange
        Direccion nueva = new Direccion();
        nueva.setPais("España");
        nueva.setCiudad("Madrid");
        nueva.setCodigoPostal("28001");
        nueva.setCalle("Gran Vía");
        nueva.setEdificio("1A");

        when(usuarioService.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/direccion", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nueva)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    public void testEditarDireccionOk() throws Exception{
        //Arrange
        Usuario user = new Usuario();
        user.setId(1L);

        Direccion nueva = new Direccion();
        nueva.setPais("España");
        nueva.setCiudad("Madrid");
        nueva.setCodigoPostal("28001");
        nueva.setCalle("Gran Vía");
        nueva.setEdificio("1A");

        when(usuarioService.findById(1L)).thenReturn(Optional.of(user));
        when(usuarioService.updateUsuario(any(Usuario.class), eq(1L))).thenReturn(user);

        //Act & Assert
        mockMvc.perform(patch("/api/auth/{id}/direccion", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nueva)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Dirección actualizada correctamente"));
    }

    @Test
    public void testDeleteUsuario() throws Exception{
        //Arrange
        when(usuarioService.findById(1L)).thenReturn(Optional.of(new Usuario()));
        doNothing().when(usuarioService).deleteUsuario(1L);

        //Act & Assert
        mockMvc.perform(delete("/api/auth/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Usuario eliminado"));
    }
}