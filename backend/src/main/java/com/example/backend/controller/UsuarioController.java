package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Usuario;
import com.example.backend.service.UsuarioService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/usuarios")
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody Usuario usuario) {
        boolean exists = usuarioService.usuarioExists(usuario.getEmail());

        String message = exists ? "❌ Usuario ya existe" : "✅ Usuario creado correctamente";
        
        if (!exists) {
            usuarioService.createUsuario(usuario);
        }

        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/api/usuarios/login")
    public ResponseEntity<Map<String, Object>> loginUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.findByEmail(usuario.getEmail());

        if (usuarioExistente == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "❌ Usuario no encontrado"));
        }

        if (!usuarioExistente.getPassword().equals(usuario.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "❌ Contraseña incorrecta"));
        }

        return ResponseEntity.ok(Map.of(
            "message", "✅ Inicio de sesión exitoso",
            "usuario", usuarioExistente
        ));
    }

    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioById(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/api/usuarios")
    public List<Usuario> showAllUsuarios() {
        return usuarioService.showAllUsuarios();
    }

    @PutMapping("/api/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> updateUsuario(@PathVariable Long id,
            @RequestBody Usuario usuarioActualizado) {

        Usuario usuario = usuarioService.updateUsuario(id, usuarioActualizado);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "❌ Usuario no encontrado"));
        }

        return ResponseEntity.ok(Map.of(
            "message", "✅ Usuario actualizado correctamente",
            "usuario", usuario
        ));
    }

    @DeleteMapping("/api/usuarios/{id}")
    public String deleteUsuario(@PathVariable("id") Long usuarioId) {
        usuarioService.deleteUsuario(usuarioId);
        return "Usuario eliminado";
    }
}
