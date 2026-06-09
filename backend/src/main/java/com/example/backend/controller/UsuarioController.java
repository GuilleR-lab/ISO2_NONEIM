package com.example.backend.controller;

import java.util.Map;
//import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
//import com.example.backend.model.Direccion;
import com.example.backend.service.UsuarioService;
import com.example.backend.dto.response.UsuarioDTO;
import com.example.backend.model.Usuario;
import com.example.backend.model.Usuario.Rol;
import com.example.backend.dto.response.JwtDTO;
import com.example.backend.security.JwtService;

@CrossOrigin(
    origins = "http://localhost:3000",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowCredentials = "true"   
)
@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService){
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        UsuarioDTO usuario = usuarioService.authenticate(dto.getIdentifier(), dto.getPassword());

        //generate token JWT
        String token = jwtService.generateToken(usuario);
        
        return ResponseEntity.ok(new JwtDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest dto) {
        usuarioService.createUsuario(dto);

        return ResponseEntity.ok(Map.of("message", "Usuario creado correctamente"));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Long id){

        //TODO: changue to return DTO not Usuario object       
        Usuario usuario = usuarioService.findById(id).get();
        
        return ResponseEntity.ok(usuario);
    }

    //@PostMapping("/refresh")
    //public void refreshToken(){
        //TODO
    //}


    @PatchMapping("/{id}/rol")
    public ResponseEntity<?> updateRol(@PathVariable Long id, @RequestBody Map<String, String> body){
        Rol newRol = Rol.valueOf(body.get("rol")); //parse String to Rol type
        
        //apply updates
        Usuario newUsuario = usuarioService.findById(id).get();
        newUsuario.setRol(newRol);

        UsuarioDTO usuario = usuarioService.updateUsuario(newUsuario, id);

        //generate new token JWT (only for username, email, rol)
        String newToken = jwtService.generateToken(usuario);

        return ResponseEntity.ok(new JwtDTO(newToken));
    }
    
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String passwordActual = body.get("passwordActual");
        String passwordNueva = body.get("passwordNueva");

        if (passwordActual == null || passwordActual.isBlank() ||
            passwordNueva == null || passwordNueva.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        try {
            usuarioService.updatePassword(id, passwordActual, passwordNueva);
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    //@PatchMapping("/{id}/direccion")
    //public ResponseEntity<?> updateDireccion(@PathVariable Long id, @RequestBody Direccion nuevaDireccion) {
    //    if (nuevaDireccion == null ||
    //        nuevaDireccion.getPais() == null || nuevaDireccion.getPais().isBlank() ||
    //        nuevaDireccion.getCiudad() == null || nuevaDireccion.getCiudad().isBlank() ||
    //        nuevaDireccion.getCodigoPostal() == null || nuevaDireccion.getCodigoPostal().isBlank() ||
    //        nuevaDireccion.getCalle() == null || nuevaDireccion.getCalle().isBlank() ||
    //        nuevaDireccion.getEdificio() == null || nuevaDireccion.getEdificio().isBlank()) {
    //        return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios de la dirección"));
    //    }

    //    Optional<Usuario> usuarioOpt = usuarioService.findById(id);
    //    if (usuarioOpt.isEmpty()) {
    //        return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
    //    }

    //    Usuario user = usuarioOpt.get();
    //    if (nuevaDireccion.getPiso() == null || nuevaDireccion.getPiso().isBlank()) {
    //        nuevaDireccion.setPiso(null);
    //    }
    //    user.setAddress(nuevaDireccion);
    //    usuarioService.updateUsuario(user, id);
    //    return ResponseEntity.ok(Map.of("message", "Dirección actualizada correctamente"));
    //}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado"));
    }
}
