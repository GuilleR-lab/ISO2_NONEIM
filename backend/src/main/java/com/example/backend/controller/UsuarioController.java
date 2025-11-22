package com.example.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.AuthDTO.LoginRequest;
import com.example.backend.model.Usuario;
import com.example.backend.service.SessionService;
import com.example.backend.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true"   
)
@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SessionService sessionService;


    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto, HttpServletResponse response) {

        // Buscar usuario por email o username
        Usuario usuario = dto.getEmail() != null
                ? usuarioService.findByEmail(dto.getEmail())
                : usuarioService.findByUsername(dto.getUsername());

        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Este usuario no existe"
            ));
        }

        // Comprobar contraseña
        if (!usuario.getPassword().equals(dto.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Contraseña incorrecta"
            ));
        }

        
        String sessionToken = UUID.randomUUID().toString();

        // Guardar sesión en memoria
        sessionService.storeSession(sessionToken, usuario);

        
        ResponseCookie cookie = ResponseCookie.from("SESSIONID", sessionToken)
                .httpOnly(true)
                .secure(false)          // PONLO EN true SI USAS HTTPS
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of(
                "message", "Inicio de sesion correcto",
                "usuario", usuario
        ));
    }



    
    @PostMapping("/register")
    public ResponseEntity<?> RegistrarUsuario(@RequestBody Usuario usuario) {

        boolean exists = usuarioService.usuarioExists(usuario.getUsername()) ||
                         usuarioService.usuarioExists(usuario.getEmail());

        if (exists) {
            return ResponseEntity.ok(Map.of(
                    "message", "Error usuario ya registrado"
            ));
        }

        usuarioService.createUsuario(usuario);

        return ResponseEntity.ok(Map.of(
                "message", "Usuario registrado correctamente"
        ));
    }



    
    @GetMapping("/me")
    public ResponseEntity<?> sessionUser(
            @CookieValue(value = "SESSIONID", required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "No autenticado"
            ));
        }

        Usuario usuario = sessionService.getUsuarioFromToken(token);

        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Sesión inválida"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Usuario autenticado",
                "usuario", usuario
        ));
    }



    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "SESSIONID", required = false) String token,
            HttpServletResponse response) {

        if (token != null) {
            sessionService.deleteSession(token);
        }

        ResponseCookie cookie = ResponseCookie.from("SESSIONID", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Sesión cerrada"));
    }



    
    @GetMapping("/login")
    public List<Usuario> showAllUsuarios() {
        return usuarioService.showAllUsuarios();
    }

    @DeleteMapping("/login/{id}")
    public String deleteUsuario(@PathVariable("id") Long usuarioId) {
        usuarioService.deleteUsuario(usuarioId);
        return "Usuario eliminado";
    }
}
