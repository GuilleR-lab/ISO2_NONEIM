package com.example.backend.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.AuthDTO.LoginRequest;
import com.example.backend.dto.AuthDTO.LoginResponse;
import com.example.backend.dto.AuthDTO.RegisterRequest;
import com.example.backend.model.Direccion;
import com.example.backend.model.Usuario;
import com.example.backend.model.Usuario.Rol;
import com.example.backend.service.UsuarioService;

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

        //Validacion de usuario y email duplicados
        boolean exists = usuarioService.usuarioExists(dto.getUsername())
                      || usuarioService.usuarioExists(dto.getEmail());

        if (exists) {
            return ResponseEntity.status(409).body(Map.of("message", "Error usuario ya registrado"));
        }

        Rol rol = Rol.INQUILINO;
        if ("PROPIETARIO".equalsIgnoreCase(dto.getRol())) {
            rol = Rol.PROPIETARIO;
        }

        Direccion address = dto.getAddress();//Obtención del objeto direccion del DTO

        //Validación de los campos obligatorios
        if (dto.getUsername() == null || dto.getUsername().isBlank() ||
            dto.getName() == null || dto.getName().isBlank() ||
            dto.getSurname() == null || dto.getSurname().isBlank() ||
            dto.getEmail() == null || dto.getEmail().isBlank() ||
            dto.getPassword() == null || dto.getPassword().isBlank() ||
            address == null ||
            address.getPais() == null || address.getPais().isBlank() ||
            address.getCiudad() == null || address.getCiudad().isBlank() ||
            address.getCodigoPostal() == null || address.getCodigoPostal().isBlank() ||
            address.getCalle() == null || address.getCalle().isBlank() ||
            address.getEdificio() == null || address.getEdificio().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        //Normalizacion del campos opcionales
        if(address.getPiso() == null || address.getPiso().isBlank()) {
            address.setPiso(null);
        }

        Usuario nuevo = new Usuario(
            dto.getUsername(),
            dto.getName(),
            dto.getSurname(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getAddress(), // el objeto de tipo Direccion 
            rol
        );

        usuarioService.createUsuario(nuevo);

        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente", "rol", rol.name()));
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id, @RequestBody Map<String, String> body){
        String nuevoRolStr = body.get("rol");
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);

        if (usuarioOpt.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }

        Usuario user = usuarioOpt.get();
        
        try{
            Rol nuevoRol = Rol.valueOf(nuevoRolStr.toUpperCase()); 
            user.setRol(nuevoRol);
            usuarioService.updateUsuario(user, id);
            return ResponseEntity.ok(Map.of(
                "message", "Rol actualizado correctamente",
                "nuevoRol", nuevoRol.name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Rol no válido"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (usuarioOpt.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(usuarioOpt.get());
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> cambiarPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String passwordActual = body.get("passwordActual");
        String passwordNueva = body.get("passwordNueva");

        if (passwordActual == null || passwordActual.isBlank() ||
            passwordNueva == null || passwordNueva.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }

        Usuario user = usuarioOpt.get();

        if (!user.getPassword().equals(passwordActual)) {
            return ResponseEntity.status(401).body(Map.of("message", "La contraseña actual no es correcta"));
        }

        user.setPassword(passwordNueva);
        usuarioService.updateUsuario(user, id);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

    @PatchMapping("/{id}/direccion")
    public ResponseEntity<?> editarDireccion(@PathVariable Long id, @RequestBody Direccion nuevaDireccion) {
        if (nuevaDireccion == null ||
            nuevaDireccion.getPais() == null || nuevaDireccion.getPais().isBlank() ||
            nuevaDireccion.getCiudad() == null || nuevaDireccion.getCiudad().isBlank() ||
            nuevaDireccion.getCodigoPostal() == null || nuevaDireccion.getCodigoPostal().isBlank() ||
            nuevaDireccion.getCalle() == null || nuevaDireccion.getCalle().isBlank() ||
            nuevaDireccion.getEdificio() == null || nuevaDireccion.getEdificio().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios de la dirección"));
        }

        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }

        Usuario user = usuarioOpt.get();
        if (nuevaDireccion.getPiso() == null || nuevaDireccion.getPiso().isBlank()) {
            nuevaDireccion.setPiso(null);
        }
        user.setAddress(nuevaDireccion);
        usuarioService.updateUsuario(user, id);
        return ResponseEntity.ok(Map.of("message", "Dirección actualizada correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado"));
    }
}