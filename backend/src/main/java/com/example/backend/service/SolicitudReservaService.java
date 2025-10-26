
package com.example.backend.service;

import com.example.backend.model.SolicitudReserva;
import com.example.backend.repository.SolicitudReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudReservaService {

    private final SolicitudReservaRepository solicitudReservaRepository;

    public SolicitudReservaService(SolicitudReservaRepository solicitudReservaRepository) {
        this.solicitudReservaRepository = solicitudReservaRepository;
    }

    // Crear una nueva solicitud
    public SolicitudReserva crearSolicitud(SolicitudReserva solicitud) {
        return solicitudReservaRepository.save(solicitud);
    }

    // Obtener todas las solicitudes
    public List<SolicitudReserva> obtenerTodas() {
        return solicitudReservaRepository.findAll();
    }

    // Buscar solicitud por ID
    public Optional<SolicitudReserva> obtenerPorId(Long idSolicitud) {
        return solicitudReservaRepository.findById(idSolicitud);
    }

    // Actualizar solicitud
    public SolicitudReserva actualizarSolicitud(Long idSolicitud, SolicitudReserva nuevaSolicitud) {
        return solicitudReservaRepository.findById(idSolicitud)
                .map(solicitud -> {
                    solicitud.setEstado(nuevaSolicitud.getEstado());
                    solicitud.setUsuario(nuevaSolicitud.getUsuario());
                    solicitud.setDisponibilidad(nuevaSolicitud.getDisponibilidad());
                    solicitud.setReserva(nuevaSolicitud.getReserva());
                    return solicitudReservaRepository.save(solicitud);
                })
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    // Eliminar solicitud
    public void eliminarSolicitud(Long idSolicitud) {
        solicitudReservaRepository.deleteById(idSolicitud);
    }
}
