package com.example.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.model.Reserva;
import com.example.backend.repository.ReservaRepository;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    @Override
    public Reserva crearReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}