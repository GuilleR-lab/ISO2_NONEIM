
package com.example.backend.controller;

import com.example.backend.model.Disponibilidad;
import com.example.backend.service.DisponibilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    // Obtener todas las disponibilidades
    @GetMapping
    public List<Disponibilidad> listar() {
        return disponibilidadService.obtenerTodas();
    }

    // Obtener una disponibilidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad> obtener(@PathVariable Long id) {
        return disponibilidadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva disponibilidad
    @PostMapping
    public Disponibilidad crear(@RequestBody Disponibilidad disponibilidad) {
        return disponibilidadService.crearDisponibilidad(disponibilidad);
    }

    // Actualizar una disponibilidad existente
    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(@PathVariable Long id, @RequestBody Disponibilidad nuevaDisponibilidad) {
        try {
            return ResponseEntity.ok(disponibilidadService.actualizarDisponibilidad(id, nuevaDisponibilidad));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una disponibilidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            disponibilidadService.eliminarDisponibilidad(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
