package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.AuthDTO.LoginRequest;
import com.example.backend.model.Usuario;
import com.example.backend.service.UsuarioService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginRequest dto) {
        boolean exists = dto.getEmail() == null
                ? usuarioService.usuarioExists(dto.getNombre())
                : usuarioService.usuarioExists(dto.getEmail());

        String message = exists ? "Inicio de sesión correcto" : "Este usuario no existe";

        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        boolean exists = usuarioService.usuarioExists(usuario.getNombre()) || usuarioService.usuarioExists(usuario.getEmail());
        String message;

        if (exists) {
            message = "❌ Error: usuario ya registrado";
        } else {
            usuarioService.createUsuario(usuario);
            message = "✅ Usuario registrado correctamente";
        }

        return ResponseEntity.ok(Map.of("message", message));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> showAllUsuarios() {
        return ResponseEntity.ok(usuarioService.showAllUsuarios());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") Long usuarioId) {
        usuarioService.deleteUsuario(usuarioId);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado"));
    }
}
