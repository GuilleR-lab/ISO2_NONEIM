package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backend.model.SolicitudReserva;

@Repository
public interface SolicitudReservaRepository extends JpaRepository<SolicitudReserva, Long> {
    List<SolicitudReserva> findByUsuarioId(Long usuarioId);
    List<SolicitudReserva> findByDisponibilidadInmueblePropietarioId(Long propietarioId);
    List<SolicitudReserva> findByEstado(String estado);
    @Query("SELECT s FROM SolicitudReserva s " +
       "JOIN FETCH s.disponibilidad d " +
       "JOIN FETCH d.inmueble i " + // Esto fuerza la carga del inmueble
       "JOIN FETCH s.usuario u " +    // De paso traemos al inquilino
       "WHERE i.propietario.id = :propietarioId " +
       "AND s.estado = 'PENDIENTE'")
    List<SolicitudReserva> obtenerConDisponibilidadInmueblePropietarioIdAndEstado(Long propietarioId, String estado);
    Long countByDisponibilidadInmueblePropietarioIdAndEstado(Long propietarioId, String estado);    
}
