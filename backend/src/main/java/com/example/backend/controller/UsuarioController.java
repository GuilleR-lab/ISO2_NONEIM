package com.example.backend.controller;

import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.AuthDTO.LoginRequest;
import com.example.backend.dto.AuthDTO.LoginResponse;
import com.example.backend.dto.AuthDTO.RegisterRequest;
import com.example.backend.model.Usuario;
import com.example.backend.model.Usuario.Rol;
import com.example.backend.service.UsuarioService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        String identifier = dto.getEmail() != null ? dto.getEmail() : dto.getUsername();

        if (identifier == null || identifier.isBlank() || dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        Optional<Usuario> usuarioOpt = usuarioService.findByEmailOrUsername(identifier);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getPassword().equals(dto.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Contraseña incorrecta"));
        }

        LoginResponse response = new LoginResponse(
            "Inicio de sesion correcto",
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getRol().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest dto) {
        if (dto.getUsername() == null || dto.getEmail() == null || dto.getPassword() == null
                || dto.getSurname() == null || dto.getAddress() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        boolean exists = usuarioService.usuarioExists(dto.getUsername())
                      || usuarioService.usuarioExists(dto.getEmail());

        if (exists) {
            return ResponseEntity.status(409).body(Map.of("message", "Error usuario ya registrado"));
        }

        Rol rol = Rol.INQUILINO;
        if ("PROPIETARIO".equalsIgnoreCase(dto.getRol())) {
            rol = Rol.PROPIETARIO;
        }

        Usuario nuevo = new Usuario(
            dto.getUsername(),
            dto.getSurname(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getAddress(),
            rol
        );

        usuarioService.createUsuario(nuevo);

        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente", "rol", rol.name()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado"));
    }
}