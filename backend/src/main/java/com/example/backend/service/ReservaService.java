package com.example.backend.service;

import java.util.List;
import com.example.backend.model.Reserva;

public interface ReservaService {
    List<Reserva> listarReservas();
    Reserva crearReserva(Reserva reserva);
    void eliminarReserva(Long id);
}