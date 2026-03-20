package com.example.backend.repository;

import com.example.backend.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Long> {

    // Búsqueda con filtros opcionales
    @Query("SELECT DISTINCT i FROM Inmueble i JOIN i.disponibilidades d " +
           "WHERE (:ciudad IS NULL OR LOWER(i.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) " +
           "AND (:tipo IS NULL OR i.tipo = :tipo) " +
           "AND (:soloDirecta = false OR d.directa = true) " +
           "AND (:fechaInicio IS NULL OR d.fechaInicio <= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR d.fechaFin >= :fechaFin)")
    List<Inmueble> buscarConFiltros(
        @Param("ciudad") String ciudad,
        @Param("tipo") Inmueble.Tipo tipo,
        @Param("soloDirecta") boolean soloDirecta,
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );

    List<Inmueble> findByPropietarioId(Long propietarioId);
}