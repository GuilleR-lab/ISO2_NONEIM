package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.AuthDTO.LoginRequest;
import com.example.backend.dto.AuthDTO.RegisterRequest;
import com.example.backend.model.Usuario;
import com.example.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

   @PostMapping("/login")
    public ResponseEntity<?> crearUsuario(@RequestBody LoginRequest dto) {
        
        boolean exists = dto.getEmail() == null ? usuarioService.usuarioExits(dto.getUsername()) : usuarioService.usuarioExits(dto.getEmail());

        String message = exists ? "Inicio de sesion correcto" : "Este usuario no existe";  //usuarioService.createUsuario(usuario);
        
        return ResponseEntity.ok(Map.of(
            "message", message
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> RegistrarUsuario(@RequestBody Usuario usuario) {
        
        
        String message = "Usuario registrado correctamente";
        
        usuarioService.createUsuario(usuario);
        
        return ResponseEntity.ok(Map.of(
            "message", message
        ));
    }

    /*@GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioById(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }*/

    @GetMapping("/login")
    public List<Usuario> showAllUsuarios() {
        return usuarioService.showAllUsuarios();
    }

    /*@PutMapping("/api/usuarios/{id}")
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
    }*/

    @DeleteMapping("/login/{id}")
    public String deleteUsuario(@PathVariable("id") Long usuarioId) {
        usuarioService.deleteUsuario(usuarioId);
        return "Usuario eliminado";
    }  
}
