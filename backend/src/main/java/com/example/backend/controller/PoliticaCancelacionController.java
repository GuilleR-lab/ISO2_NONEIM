package com.example.backend.controller;

import com.example.backend.model.PoliticaCancelacion;
import com.example.backend.service.PoliticaCancelacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/politicas")
public class PoliticaCancelacionController {

    private final PoliticaCancelacionService politicaService;

    public PoliticaCancelacionController(PoliticaCancelacionService politicaService) {
        this.politicaService = politicaService;
    }

    @PostMapping
    public ResponseEntity<PoliticaCancelacion> crearPolitica(@RequestBody PoliticaCancelacion politica) {
        return ResponseEntity.ok(politicaService.crearPolitica(politica));
    }

    @GetMapping
    public ResponseEntity<List<PoliticaCancelacion>> obtenerTodas() {
        return ResponseEntity.ok(politicaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoliticaCancelacion> obtenerPorId(@PathVariable Long id) {
        return politicaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoliticaCancelacion> actualizarPolitica(@PathVariable Long id, @RequestBody PoliticaCancelacion politica) {
        try {
            return ResponseEntity.ok(politicaService.actualizarPolitica(id, politica));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
