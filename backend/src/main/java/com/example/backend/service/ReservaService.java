
package com.example.backend.service;

import com.example.backend.model.Reserva;
import com.example.backend.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // Crear una nueva reserva
    public Reserva crearReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Obtener todas las reservas
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    // Buscar reserva por ID
    public Optional<Reserva> obtenerPorId(Long idReserva) {
        return reservaRepository.findById(idReserva);
    }

    // Actualizar una reserva existente
    public Reserva actualizarReserva(Long idReserva, Reserva nuevaReserva) {
        return reservaRepository.findById(idReserva)
                .map(reserva -> {
                    reserva.setFechaInicio(nuevaReserva.getFechaInicio());
                    reserva.setFechaFin(nuevaReserva.getFechaFin());
                    reserva.setPagado(nuevaReserva.isPagado());
                    reserva.setActiva(nuevaReserva.isActiva());
                    reserva.setDisponibilidad(nuevaReserva.getDisponibilidad());
                    reserva.setPago(nuevaReserva.getPago());
                    reserva.setPoliticaCancelacion(nuevaReserva.getPoliticaCancelacion());
                    return reservaRepository.save(reserva);
                })
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    // Eliminar una reserva
    public void eliminarReserva(Long idReserva) {
        reservaRepository.deleteById(idReserva);
    }
}
