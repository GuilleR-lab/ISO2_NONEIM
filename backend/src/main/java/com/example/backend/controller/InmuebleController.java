
package com.example.backend.controller;

import com.example.backend.model.Inmueble;
import com.example.backend.service.InmuebleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inmuebles")
public class InmuebleController {

    private final InmuebleService inmuebleService;

    public InmuebleController(InmuebleService inmuebleService) {
        this.inmuebleService = inmuebleService;
    }

    // Obtener todos los inmuebles
    @GetMapping
    public List<Inmueble> listar() {
        return inmuebleService.obtenerTodos();
    }

    // Obtener un inmueble por ID
    @GetMapping("/{id}")
    public ResponseEntity<Inmueble> obtener(@PathVariable Long id) {
        return inmuebleService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo inmueble
    @PostMapping
    public Inmueble crear(@RequestBody Inmueble inmueble) {
        return inmuebleService.crearInmueble(inmueble);
    }

    // Actualizar un inmueble existente
    @PutMapping("/{id}")
    public ResponseEntity<Inmueble> actualizar(@PathVariable Long id, @RequestBody Inmueble nuevoInmueble) {
        try {
            return ResponseEntity.ok(inmuebleService.actualizarInmueble(id, nuevoInmueble));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un inmueble
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            inmuebleService.eliminarInmueble(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
