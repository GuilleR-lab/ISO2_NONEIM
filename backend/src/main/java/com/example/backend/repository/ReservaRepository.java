package com.example.backend.repository;

import com.example.backend.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.inquilino.id = :inquilinoId")
    List<Reserva> findByInquilinoId(@Param("inquilinoId") Long inquilinoId);

    @Query("SELECT r FROM Reserva r WHERE r.inmueble.idInmueble = :inmuebleId")
    List<Reserva> findByInmuebleId(@Param("inmuebleId") Long inmuebleId);
}