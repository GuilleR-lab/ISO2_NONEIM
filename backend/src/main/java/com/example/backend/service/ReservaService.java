package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.backend.model.Reserva;
import com.example.backend.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva crearReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> obtenerPorId(Long idReserva) {
        return reservaRepository.findById(idReserva);
    }

    public List<Reserva> obtenerPorInquilino(Long inquilinoId) {
        return reservaRepository.findByInquilinoId(inquilinoId);
    }

    public List<Reserva> obtenerPorInmueble(Long inmuebleId) {
        return reservaRepository.findByInmuebleId(inmuebleId);
    }

    public void eliminarReserva(Long idReserva) {
        if(!reservaRepository.existsById(idReserva)) {
            throw new RuntimeException("Reserva no encontrada");
        }
        reservaRepository.deleteById(idReserva);
    }
}