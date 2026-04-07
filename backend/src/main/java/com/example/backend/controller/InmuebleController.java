package com.example.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.model.Usuario;
import com.example.backend.repository.PoliticaCancelacionRepository;
import com.example.backend.service.DisponibilidadService;
import com.example.backend.service.InmuebleService;
import com.example.backend.service.UsuarioService;

@RestController
@RequestMapping("/api/inmuebles")
public class InmuebleController {

    private final InmuebleService inmuebleService;
    private final UsuarioService usuarioService;
    private final DisponibilidadService disponibilidadService;
    private final PoliticaCancelacionRepository politicaCancelacionRepository;

    public InmuebleController(InmuebleService inmuebleService, UsuarioService usuarioService,
                               DisponibilidadService disponibilidadService,
                               PoliticaCancelacionRepository politicaCancelacionRepository) {
        this.inmuebleService = inmuebleService;
        this.usuarioService = usuarioService;
        this.disponibilidadService = disponibilidadService;
        this.politicaCancelacionRepository = politicaCancelacionRepository;
    }

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

    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<List<Inmueble>> obtenerPorPropietario(@PathVariable Long propietarioId) {
        return ResponseEntity.ok(inmuebleService.obtenerPorPropietario(propietarioId));
    }

    @PostMapping("/alta")
    public ResponseEntity<?> darDeAlta(@RequestBody Map<String, Object> body) {
        try {
            String[] camposObligatorios = {"propietarioId", "direccion", "ciudad", "precioNoche", "tipo", "fechaInicio", "fechaFin"};
            for (String campo : camposObligatorios) {
                if (body.get(campo) == null || body.get(campo).toString().isBlank()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "El campo '" + campo + "' es obligatorio"));
                }
            }

            Long propietarioId = Long.valueOf(body.get("propietarioId").toString());
            Optional<Usuario> propietarioOpt = usuarioService.findById(propietarioId);

            if (propietarioOpt.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("message", "Propietario no encontrado"));

            Usuario propietario = propietarioOpt.get();
            if (propietario.getRol() != Usuario.Rol.PROPIETARIO)
                return ResponseEntity.status(403).body(Map.of("message", "Solo los propietarios pueden dar de alta inmuebles"));

            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(body.get("direccion").toString());
            inmueble.setCiudad(body.get("ciudad").toString());
            inmueble.setPrecioNoche(Double.parseDouble(body.get("precioNoche").toString()));
            inmueble.setDescripcion(body.getOrDefault("descripcion", "").toString());
            String tipoStr = body.get("tipo").toString().toUpperCase().trim();
            inmueble.setTipo(Inmueble.Tipo.valueOf(tipoStr));
            inmueble.setPropietario(propietario);

            // Asignar política de cancelación
            if (body.get("idPolitica") != null && !body.get("idPolitica").toString().isBlank()) {
                Long idPolitica = Long.valueOf(body.get("idPolitica").toString());
                politicaCancelacionRepository.findById(idPolitica)
                    .ifPresent(inmueble::setPoliticaCancelacion);
            }

            Inmueble guardado = inmuebleService.crearInmueble(inmueble);

            Disponibilidad disp = new Disponibilidad();
            disp.setFechaInicio(LocalDate.parse(body.get("fechaInicio").toString()));
            disp.setFechaFin(LocalDate.parse(body.get("fechaFin").toString()));
            disp.setPrecio(inmueble.getPrecioNoche());
            Object directaVal = body.getOrDefault("reservaDirecta", body.getOrDefault("directa", "false"));
            disp.setDirecta(Boolean.parseBoolean(directaVal.toString()));
            disp.setInmueble(guardado);

            disponibilidadService.crearDisponibilidad(disp);

            return ResponseEntity.ok(Map.of(
                "message", "Inmueble creado correctamente",
                "idInmueble", guardado.getIdInmueble()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Valor de tipo de inmueble no válido"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error interno: " + e.getMessage()));
        }
    }

    @PutMapping("/alta/{id}")
    public ResponseEntity<?> editarInmueble(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Optional<Inmueble> inmuebleOpt = inmuebleService.obtenerPorId(id);
            if (inmuebleOpt.isEmpty())
                return ResponseEntity.notFound().build();

            Inmueble inmueble = inmuebleOpt.get();
            inmueble.setDireccion(body.get("direccion").toString());
            inmueble.setCiudad(body.get("ciudad").toString());
            inmueble.setPrecioNoche(Double.parseDouble(body.get("precioNoche").toString()));
            inmueble.setDescripcion(body.getOrDefault("descripcion", "").toString());
            String tipoStr = body.get("tipo").toString().toUpperCase().trim();
            inmueble.setTipo(Inmueble.Tipo.valueOf(tipoStr));

            // Actualizar política de cancelación
            if (body.get("idPolitica") != null && !body.get("idPolitica").toString().isBlank()) {
                Long idPolitica = Long.valueOf(body.get("idPolitica").toString());
                politicaCancelacionRepository.findById(idPolitica)
                    .ifPresent(inmueble::setPoliticaCancelacion);
            } else {
                inmueble.setPoliticaCancelacion(null);
            }

            Inmueble guardado = inmuebleService.actualizarInmueble(id, inmueble);

            // Actualizar disponibilidad
            if (guardado.getDisponibilidades() != null && !guardado.getDisponibilidades().isEmpty()) {
                Disponibilidad disp = guardado.getDisponibilidades().get(0);
                Object directaVal = body.getOrDefault("reservaDirecta", body.getOrDefault("directa", "false"));
                disp.setDirecta(Boolean.parseBoolean(directaVal.toString()));
                disp.setPrecio(guardado.getPrecioNoche());
                disponibilidadService.crearDisponibilidad(disp);
            }

            return ResponseEntity.ok(Map.of(
                "message", "Propiedad actualizada correctamente",
                "idInmueble", guardado.getIdInmueble()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Valor de tipo de inmueble no válido"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error interno: " + e.getMessage()));
        }
    }

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