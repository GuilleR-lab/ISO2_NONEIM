package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.SolicitudReserva;
import com.example.backend.service.SolicitudReservaService;

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
    @GetMapping("/inquilino/{id}")
    public ResponseEntity<SolicitudReserva> obtenerPorId(@PathVariable Long id) {
        return solicitudReservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar solicitud
    @PutMapping("/inquilino/{id}")
    public ResponseEntity<SolicitudReserva> actualizarSolicitud(@PathVariable Long id, @RequestBody SolicitudReserva solicitud) {
        try {
            return ResponseEntity.ok(solicitudReservaService.actualizarSolicitud(id, solicitud));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar solicitud
    @DeleteMapping("/inquilino/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudReservaService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    /* Perspectiva del propietario */
   @GetMapping("/propietario/{propietarioId}/pendientes")
    public ResponseEntity<List<SolicitudReserva>> obtenerPendientes(@PathVariable Long propietarioId) {
        List<SolicitudReserva> lista = solicitudReservaService.obtenerPendientesPropietario(propietarioId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/propietario/{propietarioId}/pendientes/count")
    public ResponseEntity<Map<String, Long>> contarPendientes(@PathVariable Long propietarioId) {
        long count = solicitudReservaService.contarPendientesPropietario(propietarioId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try{
            String nuevoEstado = body.get("estado");

            if(!nuevoEstado.equals("ACEPTADA") && !nuevoEstado.equals("RECHAZADA")){
                return ResponseEntity.badRequest().body(Map.of("message", "Estado inválido"));
            }

            SolicitudReserva solicitudActualizada = solicitudReservaService.cambiarEstadoSolicitud(id, nuevoEstado);
            return ResponseEntity.ok(solicitudActualizada);
        }catch(RuntimeException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
        

    }
}
