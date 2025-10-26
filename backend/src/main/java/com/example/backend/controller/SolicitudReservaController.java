package com.example.backend.controller;

import com.example.backend.model.SolicitudReserva;
import com.example.backend.service.SolicitudReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudReservaController {

    private final SolicitudReservaService solicitudReservaService;

    public SolicitudReservaController(SolicitudReservaService solicitudReservaService) {
        this.solicitudReservaService = solicitudReservaService;
    }

    // Crear solicitud
    @PostMapping
    public ResponseEntity<SolicitudReserva> crearSolicitud(@RequestBody SolicitudReserva solicitud) {
        return ResponseEntity.ok(solicitudReservaService.crearSolicitud(solicitud));
    }

    // Obtener todas las solicitudes
    @GetMapping
    public ResponseEntity<List<SolicitudReserva>> obtenerTodas() {
        return ResponseEntity.ok(solicitudReservaService.obtenerTodas());
    }

    // Obtener solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudReserva> obtenerPorId(@PathVariable Long id) {
        return solicitudReservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar solicitud
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudReserva> actualizarSolicitud(@PathVariable Long id, @RequestBody SolicitudReserva solicitud) {
        try {
            return ResponseEntity.ok(solicitudReservaService.actualizarSolicitud(id, solicitud));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar solicitud
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudReservaService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
