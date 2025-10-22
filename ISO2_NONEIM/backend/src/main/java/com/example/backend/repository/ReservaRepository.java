package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}