package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;
    private final InmuebleRepository inmuebleRepository;
    private final PagoRepository pagoRepository;
    private final SolicitudReservaRepository solicitudReservaRepository;
    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaService reservaService,
                             UsuarioRepository usuarioRepository,
                             InmuebleRepository inmuebleRepository,
                             PagoRepository pagoRepository,
                             SolicitudReservaRepository solicitudReservaRepository,
                             ReservaRepository reservaRepository) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
        this.inmuebleRepository = inmuebleRepository;
        this.pagoRepository = pagoRepository;
        this.solicitudReservaRepository = solicitudReservaRepository;
        this.reservaRepository = reservaRepository;
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> reservar(@RequestBody Map<String, Object> body) {
        try {
            Long usuarioId = Long.valueOf(body.get("inquilinoId").toString());
            Long inmuebleId = Long.valueOf(body.get("inmuebleId").toString());
            LocalDate fechaInicio = LocalDate.parse(body.get("fechaInicio").toString());
            LocalDate fechaFin = LocalDate.parse(body.get("fechaFin").toString());
            String metodoPago = body.get("metodoPago").toString();

            // Validar usuario
            Optional<Usuario> inquilinoOpt = usuarioRepository.findById(usuarioId);
            if (inquilinoOpt.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("message", "Usuario no encontrado"));

            Usuario inquilino = inquilinoOpt.get();

            // Validar inmueble
            Optional<Inmueble> inmuebleOpt = inmuebleRepository.findById(inmuebleId);
            if (inmuebleOpt.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("message", "Inmueble no encontrado"));

            Inmueble inmueble = inmuebleOpt.get();

            // No puede reservar su propio inmueble
            if (inmueble.getPropietario().getId().equals(usuarioId))
                return ResponseEntity.status(403).body(Map.of("message", "No puedes reservar tu propio inmueble"));

            // Buscar disponibilidad válida para esas fechas
            Optional<Disponibilidad> dispOpt = inmueble.getDisponibilidades().stream()
                .filter(d -> !d.getFechaInicio().isAfter(fechaInicio) && !d.getFechaFin().isBefore(fechaFin))
                .findFirst();

            if (dispOpt.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("message", "No hay disponibilidad para esas fechas"));

            Disponibilidad disponibilidad = dispOpt.get();

            // Calcular importe
            long dias = fechaInicio.until(fechaFin).getDays();
            if (dias <= 0)
                return ResponseEntity.badRequest().body(Map.of("message", "Las fechas no son válidas"));

            double importe = dias * inmueble.getPrecioNoche();

            // Validar que no hay reservas solapadas
            List<Reserva> reservasExistentes = reservaRepository.findByInmuebleId(inmuebleId);
            boolean hayConflicto = reservasExistentes.stream()
                .anyMatch(r ->
                    !r.getFechaFin().isBefore(fechaInicio) &&
                    !r.getFechaInicio().isAfter(fechaFin)
                );

            if (hayConflicto)
                return ResponseEntity.badRequest().body(Map.of("message", "Ya existe una reserva para esas fechas"));

            // RESERVA DIRECTA
            if (disponibilidad.isDirecta()) {
                Reserva reserva = new Reserva();
                reserva.setFechaInicio(fechaInicio);
                reserva.setFechaFin(fechaFin);
                reserva.setActiva(true);
                reserva.setPagado(true);
                reserva.setInquilino(inquilino);
                reserva.setInmueble(inmueble);
                reserva.setDisponibilidad(disponibilidad);

                Reserva guardada = reservaService.crearReserva(reserva);
                Pago pago = new Pago(metodoPago, importe, guardada);
                pagoRepository.save(pago);

                return ResponseEntity.ok(Map.of(
                    "message", "Reserva confirmada",
                    "tipo", "DIRECTA",
                    "idReserva", guardada.getIdReserva(),
                    "importe", importe
                ));

            // SOLICITUD DE RESERVA
            } else {
                SolicitudReserva solicitud = new SolicitudReserva();
                solicitud.setEstado("PENDIENTE");
                solicitud.setUsuario(inquilino);
                solicitud.setDisponibilidad(disponibilidad);
                solicitud.setFechaInicio(fechaInicio);
                solicitud.setFechaFin(fechaFin);

                SolicitudReserva solicitudGuardada = solicitudReservaRepository.save(solicitud);

                Reserva reserva = new Reserva();
                reserva.setFechaInicio(fechaInicio);
                reserva.setFechaFin(fechaFin);
                reserva.setActiva(false);
                reserva.setPagado(true);
                reserva.setInquilino(inquilino);
                reserva.setInmueble(inmueble);
                reserva.setDisponibilidad(disponibilidad);
                reserva.setSolicitudReserva(solicitudGuardada);

                Reserva reservaGuardada = reservaService.crearReserva(reserva);
                Pago pago = new Pago(metodoPago, importe, reservaGuardada);
                pagoRepository.save(pago);

                return ResponseEntity.ok(Map.of(
                    "message", "Solicitud enviada al propietario, pendiente de confirmación",
                    "tipo", "SOLICITUD",
                    "idSolicitud", solicitudGuardada.getIdSolicitud(),
                    "importe", importe
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    // Obtener reservas de un inquilino
    @GetMapping("/inquilino/{inquilinoId}")
    public ResponseEntity<List<Reserva>> obtenerPorInquilino(@PathVariable Long inquilinoId) {
        return ResponseEntity.ok(reservaService.obtenerPorInquilino(inquilinoId));
    }

    // CRUD básico
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}