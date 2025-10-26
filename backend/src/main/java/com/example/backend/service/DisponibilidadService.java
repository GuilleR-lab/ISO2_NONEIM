
package com.example.backend.service;

import com.example.backend.model.Disponibilidad;
import com.example.backend.repository.DisponibilidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
    }

    // Crear una disponibilidad
    public Disponibilidad crearDisponibilidad(Disponibilidad disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

    // Obtener todas las disponibilidades
    public List<Disponibilidad> obtenerTodas() {
        return disponibilidadRepository.findAll();
    }

    // Buscar disponibilidad por ID
    public Optional<Disponibilidad> obtenerPorId(Long id) {
        return disponibilidadRepository.findById(id);
    }

    // Actualizar disponibilidad
    public Disponibilidad actualizarDisponibilidad(Long id, Disponibilidad nuevaDisponibilidad) {
        return disponibilidadRepository.findById(id)
                .map(disponibilidad -> {
                    disponibilidad.setFechaInicio(nuevaDisponibilidad.getFechaInicio());
                    disponibilidad.setFechaFin(nuevaDisponibilidad.getFechaFin());
                    disponibilidad.setPrecio(nuevaDisponibilidad.getPrecio());
                    disponibilidad.setDirecta(nuevaDisponibilidad.isDirecta());
                    disponibilidad.setInmueble(nuevaDisponibilidad.getInmueble());
                    return disponibilidadRepository.save(disponibilidad);
                })
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
    }

    // Eliminar disponibilidad
    public void eliminarDisponibilidad(Long id) {
        if (!disponibilidadRepository.existsById(id)) {
            throw new RuntimeException("Disponibilidad no encontrada");
        }
        disponibilidadRepository.deleteById(id);
    }
}
