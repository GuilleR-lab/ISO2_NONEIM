package com.example.backend.service;

import com.example.backend.model.Inmueble;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InmuebleService {
    Inmueble crearInmueble(Inmueble inmueble);
    List<Inmueble> obtenerTodos();
    Optional<Inmueble> obtenerPorId(Long id);
    Inmueble actualizarInmueble(Long id, Inmueble nuevoInmueble);
    void eliminarInmueble(Long id);
    List<Inmueble> buscarConFiltros(String ciudad, Inmueble.Tipo tipo, boolean soloDirecta, LocalDate fechaInicio, LocalDate fechaFin);
    List<Inmueble> obtenerPorPropietario(Long propietarioId);
}