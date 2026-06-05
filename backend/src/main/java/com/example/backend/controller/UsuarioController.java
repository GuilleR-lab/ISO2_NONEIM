package com.example.backend.controller;

import java.util.Map;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
//import com.example.backend.model.Direccion;
//import com.example.backend.model.Usuario;
//import com.example.backend.model.Usuario.Rol;
import com.example.backend.service.UsuarioService;
import com.example.backend.dto.response.UsuarioDTO;

@CrossOrigin(
    origins = "http://localhost:3000",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowCredentials = "true"   
)
@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        UsuarioDTO usuario = usuarioService.authenticate(dto.getIdentifier(), dto.getPassword());
        
        //TODO: manage session cookies

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest dto) {
        //TODO: poner todos los campos del form de registro como obligatorios en el frontend

        usuarioService.createUsuario(dto);

        return ResponseEntity.ok().build();
    }

    //@PatchMapping("/{id}/rol")
    //public ResponseEntity<?> cambiarRol(@PathVariable Long id, @RequestBody Map<String, String> body){
    //    String nuevoRolStr = body.get("rol");
    //    Optional<Usuario> usuarioOpt = usuarioService.findById(id);

    //    if (usuarioOpt.isEmpty()){
    //        return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
    //    }

    //    Usuario user = usuarioOpt.get();
        
    //    try{
    //        Rol nuevoRol = Rol.valueOf(nuevoRolStr.toUpperCase()); 
    //        user.setRol(nuevoRol);
    //        usuarioService.updateUsuario(user, id);
    //        return ResponseEntity.ok(Map.of(
    //            "message", "Rol actualizado correctamente",
    //            "nuevoRol", nuevoRol.name()
    //        ));
    //    } catch (IllegalArgumentException e) {
    //        return ResponseEntity.badRequest().body(Map.of("message", "Rol no válido"));
    //    }
    //}
    
    //@PatchMapping("/{id}/password")
    //public ResponseEntity<?> cambiarPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
    //    String passwordActual = body.get("passwordActual");
    //    String passwordNueva = body.get("passwordNueva");

    //    if (passwordActual == null || passwordActual.isBlank() ||
    //        passwordNueva == null || passwordNueva.isBlank()) {
    //        return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
    //    }

    //    Optional<Usuario> usuarioOpt = usuarioService.findById(id);
    //    if (usuarioOpt.isEmpty()) {
    //        return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
    //    }

    //    Usuario user = usuarioOpt.get();

    //    if (!user.getPassword().equals(passwordActual)) {
    //        return ResponseEntity.status(401).body(Map.of("message", "La contraseña actual no es correcta"));
    //    }

    //    user.setPassword(passwordNueva);
    //    usuarioService.updateUsuario(user, id);
    //    return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    //}

    //@PatchMapping("/{id}/direccion")
    //public ResponseEntity<?> editarDireccion(@PathVariable Long id, @RequestBody Direccion nuevaDireccion) {
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
