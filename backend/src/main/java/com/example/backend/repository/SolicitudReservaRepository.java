package com.example.backend.repository;

import com.example.backend.model.SolicitudReserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudReservaRepository extends JpaRepository<SolicitudReserva, Long> {
}
