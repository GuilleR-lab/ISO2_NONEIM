package com.example.backend.controller;

import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.model.Usuario;
import com.example.backend.service.DisponibilidadService;
import com.example.backend.service.InmuebleService;
import com.example.backend.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inmuebles")
public class InmuebleController {

    private final InmuebleService inmuebleService;
    private final UsuarioService usuarioService;
    private final DisponibilidadService disponibilidadService;

    public InmuebleController(InmuebleService inmuebleService, UsuarioService usuarioService, DisponibilidadService disponibilidadService) {
        this.inmuebleService = inmuebleService;
        this.usuarioService = usuarioService;
        this.disponibilidadService = disponibilidadService;
    }

    // Búsqueda pública con filtros opcionales
    @GetMapping("/buscar")
    public ResponseEntity<List<Inmueble>> buscar(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false, defaultValue = "false") boolean soloDirecta,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        Inmueble.Tipo tipoEnum = null;
        if (tipo != null && !tipo.isBlank()) {
            try { tipoEnum = Inmueble.Tipo.valueOf(tipo); } catch (Exception ignored) {}
        }

        LocalDate inicio = fechaInicio != null && !fechaInicio.isBlank() ? LocalDate.parse(fechaInicio) : null;
        LocalDate fin = fechaFin != null && !fechaFin.isBlank() ? LocalDate.parse(fechaFin) : null;

        return ResponseEntity.ok(inmuebleService.buscarConFiltros(ciudad, tipoEnum, soloDirecta, inicio, fin));
    }

    // Obtener inmuebles de un propietario
    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<List<Inmueble>> obtenerPorPropietario(@PathVariable Long propietarioId) {
        return ResponseEntity.ok(inmuebleService.obtenerPorPropietario(propietarioId));
    }

    // Dar de alta un inmueble con disponibilidad (solo propietarios)
    @PostMapping("/alta")
    public ResponseEntity<?> darDeAlta(@RequestBody Map<String, Object> body) {
        try {
            Long propietarioId = Long.valueOf(body.get("propietarioId").toString());
            Optional<Usuario> propietarioOpt = usuarioService.findById(propietarioId);

            if (propietarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Propietario no encontrado"));
            }

            Usuario propietario = propietarioOpt.get();
            if (propietario.getRol() != Usuario.Rol.PROPIETARIO) {
                return ResponseEntity.status(403).body(Map.of("message", "Solo los propietarios pueden dar de alta inmuebles"));
            }

            // Crear el inmueble
            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(body.get("direccion").toString());
            inmueble.setCiudad(body.get("ciudad").toString());
            inmueble.setPrecioNoche(Double.parseDouble(body.get("precioNoche").toString()));
            inmueble.setDescripcion(body.getOrDefault("descripcion", "").toString());
            inmueble.setTipo(Inmueble.Tipo.valueOf(body.getOrDefault("tipo", "VIVIENDA_COMPLETA").toString()));
            inmueble.setPropietario(propietario);

            Inmueble guardado = inmuebleService.crearInmueble(inmueble);

            // Crear la disponibilidad asociada
            Disponibilidad disp = new Disponibilidad();
            disp.setFechaInicio(LocalDate.parse(body.get("fechaInicio").toString()));
            disp.setFechaFin(LocalDate.parse(body.get("fechaFin").toString()));
            disp.setPrecio(Double.parseDouble(body.get("precioNoche").toString()));
            disp.setDirecta(Boolean.parseBoolean(body.getOrDefault("directa", "true").toString()));
            disp.setInmueble(guardado);

            disponibilidadService.crearDisponibilidad(disp);

            return ResponseEntity.ok(Map.of("message", "Inmueble creado correctamente", "idInmueble", guardado.getIdInmueble()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error al crear el inmueble: " + e.getMessage()));
        }
    }

    // CRUD básico
    @GetMapping
    public List<Inmueble> listar() {
        return inmuebleService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inmueble> obtener(@PathVariable Long id) {
        return inmuebleService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inmueble> actualizar(@PathVariable Long id, @RequestBody Inmueble nuevoInmueble) {
        try {
            return ResponseEntity.ok(inmuebleService.actualizarInmueble(id, nuevoInmueble));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            inmuebleService.eliminarInmueble(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}