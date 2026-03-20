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

    public void eliminarReserva(Long idReserva) {
        reservaRepository.deleteById(idReserva);
    }
}