package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.model.Propiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
}