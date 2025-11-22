package com.example.backend.controller;

import com.example.backend.model.Propietario;
import com.example.backend.model.Usuario;
import com.example.backend.repository.PropietarioRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;;

@RestController
@RequestMapping("/api/propietarios")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PropietarioController {

    private final PropietarioRepository propietarioRepo;
    private final SessionService sessionService;

    public PropietarioController(PropietarioRepository propietarioRepo,
                                 SessionService sessionService) {
        this.propietarioRepo = propietarioRepo;
        this.sessionService = sessionService;
    }

    @PostMapping("/activar")
    public ResponseEntity<?> activarPropietario(
            @CookieValue(value = "SESSIONID", required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        Usuario usuario = sessionService.getUsuarioFromToken(token);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Sesión inválida");
        }

        if (!propietarioRepo.existsByUsuario(usuario)) {
            propietarioRepo.save(new Propietario(usuario));
        }

        return ResponseEntity.ok("Usuario ahora es propietario");
    }

    @GetMapping("/esPropietario")
    public ResponseEntity<?> esPropietario(
            @CookieValue(value = "SESSIONID", required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(401).body(false);
        }

        Usuario usuario = sessionService.getUsuarioFromToken(token);
        if (usuario == null) {
            return ResponseEntity.status(401).body(false);
        }

        boolean esPropietario = propietarioRepo.existsByUsuario(usuario);
        return ResponseEntity.ok(esPropietario);
    }
}


    //Desactivar propietario--> dejar de ser propietario
    //    >Desactivar propietario con inmuebles dados de alta?
    //    >Eliminar inmuebles dados de alta?        
    
}
