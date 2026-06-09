package com.example.backend.repository;

import com.example.backend.model.ListaDeseos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListaDeseosRepository extends JpaRepository<ListaDeseos, Long> {
	Optional<ListaDeseos> findByUsuarioId(Long usuarioId);
	List<ListaDeseos> findAllByUsuarioId(Long usuarioId);
}
