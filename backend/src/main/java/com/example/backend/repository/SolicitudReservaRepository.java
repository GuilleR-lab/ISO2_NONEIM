package com.example.backend.repository;

import com.example.backend.model.SolicitudReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudReservaRepository extends JpaRepository<SolicitudReserva, Long> {
    List<SolicitudReserva> findByUsuarioId(Long usuarioId);
    List<SolicitudReserva> findByDisponibilidadInmueblePropietarioId(Long propietarioId);
    List<SolicitudReserva> findByEstado(String estado);
}