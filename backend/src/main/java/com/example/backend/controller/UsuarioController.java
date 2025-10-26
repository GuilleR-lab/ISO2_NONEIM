package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Usuario;
import com.example.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/usuarios")
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody Usuario usuario) {
        
        boolean exists = usuarioService.usuarioExits(usuario.getEmail());

        String message = exists ? "❌ Usuario ya existe" : "✅ Usuario creado correctamente";  //usuarioService.createUsuario(usuario);
        
<<<<<<< HEAD
        if (!exists) {
            usuarioService.createUsuario(usuario);
=======
        return ResponseEntity.ok(Map.of(
            "message", message
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> RegistrarUsuario(@RequestBody Usuario usuario) {
        String message;
        boolean exists = usuarioService.usuarioExists(usuario.getUsername()) || usuarioService.usuarioExists(usuario.getEmail());

        if (exists){
            message = "Error usuario ya registrado";
        }else {
            message = "Usuario registrado correctamente";
            usuarioService.createUsuario(usuario);
        }
        
        return ResponseEntity.ok(Map.of(
            "message", message
        ));
    }

    /*@GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioById(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
>>>>>>> feature/backend-api-integration
        }

        return ResponseEntity.ok(Map.of(
            "message", message
        ));
    }
    
    @GetMapping("/api/usuarios")
    public List<Usuario> showAllUsuarios() {
        return usuarioService.showAllUsuarios();
    }

    @DeleteMapping("/api/usuarios/{id}")
    public String deleteUsuario(@PathVariable("id") Long usuarioId) {
        usuarioService.deleteUsuario(usuarioId);
        return "Usuario eliminado";
    }   
}
