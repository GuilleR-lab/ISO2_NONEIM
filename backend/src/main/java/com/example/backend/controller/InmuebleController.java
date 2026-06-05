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

    // Dar de alta un inmueble con disponibilidad
    @PostMapping("/alta")
    public ResponseEntity<Object> darDeAlta(@RequestBody Map<String, Object> body) {
        ResponseEntity<Object> campoError = validarCamposAlta(body);
        if (campoError != null) {
            return campoError;
        }
        try {
            return procesarAltaInmueble(body);
        } catch (Exception e) {
            return manejarExcepcionInmueble(e);
        }
    }

    private static ResponseEntity<Object> manejarExcepcionInmueble(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return ResponseEntity.badRequest().body(Map.of("message", "Valor de tipo de inmueble no válido"));
        }
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("message", "Error interno: " + e.getMessage()));
    }

    private ResponseEntity<Object> procesarAltaInmueble(Map<String, Object> body) {
        Long propietarioId = Long.valueOf(body.get("propietarioId").toString());
        Optional<Usuario> propietarioOpt = usuarioService.findById(propietarioId);
        if (propietarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Propietario no encontrado"));
        }
        Usuario propietario = propietarioOpt.get();
        if (propietario.getRol() != Usuario.Rol.PROPIETARIO) {
            return ResponseEntity.status(403).body(Map.of("message", "Solo los propietarios pueden dar de alta inmuebles"));
        }
        Inmueble guardado = inmuebleService.crearInmueble(construirInmueble(body, propietario));
        crearDisponibilidadParaInmueble(body, guardado);
        return ResponseEntity.ok(Map.of(
            "message", "Inmueble creado correctamente",
            "idInmueble", guardado.getIdInmueble()
        ));
    }

    private static ResponseEntity<Object> validarCamposAlta(Map<String, Object> body) {
        String[] camposObligatorios = {"propietarioId", "direccion", "ciudad", "precioNoche", "tipo", "fechaInicio", "fechaFin"};
        for (String campo : camposObligatorios) {
            if (body.get(campo) == null || body.get(campo).toString().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "El campo '" + campo + "' es obligatorio"));
            }
        }
        return null;
    }

    private static Inmueble construirInmueble(Map<String, Object> body, Usuario propietario) {
        Inmueble inmueble = new Inmueble();
        inmueble.setDireccion(body.get("direccion").toString());
        inmueble.setCiudad(body.get("ciudad").toString());
        inmueble.setPrecioNoche(Double.parseDouble(body.get("precioNoche").toString()));
        inmueble.setDescripcion(body.getOrDefault("descripcion", "").toString());
        inmueble.setTipo(Inmueble.Tipo.valueOf(body.get("tipo").toString().toUpperCase().trim()));
        inmueble.setPropietario(propietario);
        return inmueble;
    }

    private void crearDisponibilidadParaInmueble(Map<String, Object> body, Inmueble guardado) {
        Disponibilidad disp = new Disponibilidad();
        disp.setFechaInicio(LocalDate.parse(body.get("fechaInicio").toString()));
        disp.setFechaFin(LocalDate.parse(body.get("fechaFin").toString()));
        disp.setPrecio(guardado.getPrecioNoche());
        Object directaVal = body.getOrDefault("reservaDirecta", body.getOrDefault("directa", "false"));
        disp.setDirecta(Boolean.parseBoolean(directaVal.toString()));
        disp.setInmueble(guardado);
        disponibilidadService.crearDisponibilidad(disp);
    }

    // Editar un inmueble existente
    @PutMapping("/alta/{id}")
    public ResponseEntity<Object> editarInmueble(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Inmueble> inmuebleOpt = inmuebleService.obtenerPorId(id);
        if (inmuebleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            return procesarEdicionInmueble(inmuebleOpt.get(), body);
        } catch (Exception e) {
            return manejarExcepcionInmueble(e);
        }
    }

    private ResponseEntity<Object> procesarEdicionInmueble(Inmueble inmueble, Map<String, Object> body) {
        inmueble.setDireccion(body.get("direccion").toString());
        inmueble.setCiudad(body.get("ciudad").toString());
        inmueble.setPrecioNoche(Double.parseDouble(body.get("precioNoche").toString()));
        inmueble.setDescripcion(body.getOrDefault("descripcion", "").toString());
        inmueble.setTipo(Inmueble.Tipo.valueOf(body.get("tipo").toString().toUpperCase().trim()));
        Inmueble guardado = inmuebleService.crearInmueble(inmueble);
        actualizarDisponibilidadSiExiste(guardado, body);
        return ResponseEntity.ok(Map.of(
            "message", "Propiedad actualizada correctamente",
            "idInmueble", guardado.getIdInmueble()
        ));
    }

    private void actualizarDisponibilidadSiExiste(Inmueble guardado, Map<String, Object> body) {
        if (guardado.getDisponibilidades() != null && !guardado.getDisponibilidades().isEmpty()) {
            Disponibilidad disp = guardado.getDisponibilidades().get(0);
            Object directaVal = body.getOrDefault("reservaDirecta", body.getOrDefault("directa", "false"));
            disp.setDirecta(Boolean.parseBoolean(directaVal.toString()));
            disp.setPrecio(guardado.getPrecioNoche());
            disponibilidadService.crearDisponibilidad(disp);
        }
    }

    // CRUD básico
    @GetMapping
    public List<Inmueble> listar() {
        return inmuebleService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inmueble> obtenerInmueble(@PathVariable Long id) {
        return inmuebleService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inmueble> actualizarInmueble(@PathVariable Long id, @RequestBody Inmueble nuevoInmueble) {
        try {
            return ResponseEntity.ok(inmuebleService.actualizarInmueble(id, nuevoInmueble));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInmueble(@PathVariable Long id) {
        try {
            inmuebleService.eliminarInmueble(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}