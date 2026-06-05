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
    public ResponseEntity<Object> login(@RequestBody LoginRequest dto) {
        String identifier = dto.getEmail() != null ? dto.getEmail() : dto.getUsername();

        if (identifier == null || identifier.isBlank() || dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        Optional<Usuario> usuarioOpt = usuarioService.findByEmailOrUsername(identifier);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Usuario no encontrado"));
        }

        return autenticarUsuario(usuarioOpt.get(), dto.getPassword());
    }

    private static ResponseEntity<Object> autenticarUsuario(Usuario usuario, String password) {
        if (!usuario.getPassword().equals(password)) {
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
    public ResponseEntity<Object> register(@RequestBody RegisterRequest dto) {

        boolean exists = usuarioService.usuarioExists(dto.getUsername())
                      || usuarioService.usuarioExists(dto.getEmail());
        if (exists) {
            return ResponseEntity.status(409).body(Map.of("message", "Error usuario ya registrado"));
        }

        Direccion address = dto.getAddress();
        if (!sonCamposRegistroValidos(dto, address)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        normalizarPiso(address);

        Rol rol = "PROPIETARIO".equalsIgnoreCase(dto.getRol()) ? Rol.PROPIETARIO : Rol.INQUILINO;

        Usuario nuevo = new Usuario(
            dto.getUsername(),
            dto.getName(),
            dto.getSurname(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getAddress(),
            rol
        );

        usuarioService.createUsuario(nuevo);
        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente", "rol", rol.name()));
    }

    private static boolean sonCamposRegistroValidos(RegisterRequest dto, Direccion address) {
        return tieneCamposPersonalesValidos(dto) && esDireccionValida(address);
    }

    private static boolean tieneCamposPersonalesValidos(RegisterRequest dto) {
        return tieneCredencialesValidas(dto) && tieneDatosPersonalesValidos(dto);
    }

    private static boolean tieneCredencialesValidas(RegisterRequest dto) {
        return tieneUsernameValido(dto) && tieneEmailYPasswordValidos(dto);
    }

    private static boolean tieneUsernameValido(RegisterRequest dto) {
        return dto.getUsername() != null && !dto.getUsername().isBlank();
    }

    private static boolean tieneEmailYPasswordValidos(RegisterRequest dto) {
        return dto.getEmail() != null && !dto.getEmail().isBlank()
            && dto.getPassword() != null && !dto.getPassword().isBlank();
    }

    private static boolean tieneDatosPersonalesValidos(RegisterRequest dto) {
        return dto.getName() != null && !dto.getName().isBlank()
            && dto.getSurname() != null && !dto.getSurname().isBlank();
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<Object> cambiarRol(@PathVariable Long id, @RequestBody Map<String, String> body){
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (usuarioOpt.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }
        return aplicarCambioRol(usuarioOpt.get(), body.get("rol"), id);
    }

    private ResponseEntity<Object> aplicarCambioRol(Usuario user, String nuevoRolStr, Long id) {
        try {
            Rol nuevoRol = Rol.valueOf(nuevoRolStr.toUpperCase());
            user.setRol(nuevoRol);
            usuarioService.updateUsuario(user, id);
            return ResponseEntity.ok(Map.of("message", "Rol actualizado correctamente", "nuevoRol", nuevoRol.name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Rol no válido"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (usuarioOpt.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(usuarioOpt.get());
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Object> cambiarPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
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

        return aplicarCambioPassword(usuarioOpt.get(), passwordActual, passwordNueva, id);
    }

    private ResponseEntity<Object> aplicarCambioPassword(
            Usuario user, String passwordActual, String passwordNueva, Long id) {
        if (!user.getPassword().equals(passwordActual)) {
            return ResponseEntity.status(401).body(Map.of("message", "La contraseña actual no es correcta"));
        }
        user.setPassword(passwordNueva);
        usuarioService.updateUsuario(user, id);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

    @PatchMapping("/{id}/direccion")
    public ResponseEntity<Object> editarDireccion(@PathVariable Long id, @RequestBody Direccion nuevaDireccion) {
        if (!esDireccionValida(nuevaDireccion)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios de la dirección"));
        }

        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }

        Usuario user = usuarioOpt.get();
        normalizarPiso(nuevaDireccion);
        user.setAddress(nuevaDireccion);
        usuarioService.updateUsuario(user, id);
        return ResponseEntity.ok(Map.of("message", "Dirección actualizada correctamente"));
    }

    private static boolean esDireccionValida(Direccion d) {
        if (d == null) return false;
        return tieneCamposObligatoriosDireccion(d) && tieneCamposUbicacion(d);
    }

    private static boolean tieneCamposObligatoriosDireccion(Direccion d) {
        return d.getCalle() != null && !d.getCalle().isBlank()
            && d.getEdificio() != null && !d.getEdificio().isBlank();
    }

    private static boolean tieneCamposUbicacion(Direccion d) {
        return tienePaisYCiudad(d) && d.getCodigoPostal() != null && !d.getCodigoPostal().isBlank();
    }

    private static boolean tienePaisYCiudad(Direccion d) {
        return d.getPais() != null && !d.getPais().isBlank()
            && d.getCiudad() != null && !d.getCiudad().isBlank();
    }

    private static void normalizarPiso(Direccion d) {
        if (d.getPiso() == null || d.getPiso().isBlank()) {
            d.setPiso(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado"));
    }
}