package com.example.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.model.Usuario;
import com.example.backend.service.DisponibilidadService;
import com.example.backend.service.InmuebleService;
import com.example.backend.service.UsuarioService;

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
            // --- 1. Verificación de Campos Obligatorios ---
            String[] camposObligatorios = {"propietarioId", "direccion", "ciudad", "precioNoche", "tipo", "fechaInicio", "fechaFin"};
            for (String campo : camposObligatorios) {
                if (body.get(campo) == null || body.get(campo).toString().isBlank()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "El campo '" + campo + "' es obligatorio"));
                }
            }

            // --- 2. Procesamiento del Propietario ---
            Long propietarioId = Long.valueOf(body.get("propietarioId").toString());
            Optional<Usuario> propietarioOpt = usuarioService.findById(propietarioId);

            if (propietarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Propietario no encontrado"));
            }

            Usuario propietario = propietarioOpt.get();
            if (propietario.getRol() != Usuario.Rol.PROPIETARIO) {
                return ResponseEntity.status(403).body(Map.of("message", "Solo los propietarios pueden dar de alta inmuebles"));
            }

            // --- 3. Crear el Inmueble de forma segura ---
            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(body.get("direccion").toString());
            inmueble.setCiudad(body.get("ciudad").toString());
            inmueble.setPrecioNoche(Double.parseDouble(body.get("precioNoche").toString()));
            inmueble.setDescripcion(body.getOrDefault("descripcion", "").toString());
            
            // Manejo de Enum más robusto (ignora mayúsculas/minúsculas del front)
            String tipoStr = body.get("tipo").toString().toUpperCase().trim();
            inmueble.setTipo(Inmueble.Tipo.valueOf(tipoStr));
            
            inmueble.setPropietario(propietario);

            // Guardar inmueble
            Inmueble guardado = inmuebleService.crearInmueble(inmueble);

            // --- 4. Crear la Disponibilidad ---
            Disponibilidad disp = new Disponibilidad();
            disp.setFechaInicio(LocalDate.parse(body.get("fechaInicio").toString()));
            disp.setFechaFin(LocalDate.parse(body.get("fechaFin").toString()));
            disp.setPrecio(inmueble.getPrecioNoche());
            disp.setDirecta(Boolean.parseBoolean(body.getOrDefault("directa", "true").toString()));
            disp.setInmueble(guardado);

            disponibilidadService.crearDisponibilidad(disp);

            return ResponseEntity.ok(Map.of(
                "message", "Inmueble creado correctamente", 
                "idInmueble", guardado.getIdInmueble()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Valor de tipo de inmueble no válido"));
        } catch (Exception e) {
            // Imprimimos el error en consola para que tú como dev sepas qué pasó exactamente
            e.printStackTrace(); 
            return ResponseEntity.status(500).body(Map.of("message", "Error interno: " + e.getMessage()));
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