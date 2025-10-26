package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.backend.model.Propiedad;
import com.example.backend.service.PropiedadService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {

    private final PropiedadService propiedadService;

    public PropiedadController(PropiedadService propiedadService) {
        this.propiedadService = propiedadService;
    }

    @GetMapping
    public List<Propiedad> listar() {
        return propiedadService.listarPropiedades();
    }

    @PostMapping
    public Propiedad crear(@RequestBody Propiedad propiedad) {
        return propiedadService.crearPropiedad(propiedad);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        propiedadService.eliminarPropiedad(id);
    }
}