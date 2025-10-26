package com.example.backend.controller;

import com.example.backend.model.ListaDeseos;
import com.example.backend.model.Inmueble;
import com.example.backend.service.ListaDeseosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas-deseos")
public class ListaDeseosController {

    private final ListaDeseosService listaDeseosService;

    public ListaDeseosController(ListaDeseosService listaDeseosService) {
        this.listaDeseosService = listaDeseosService;
    }

    @PostMapping
    public ResponseEntity<ListaDeseos> crearLista(@RequestBody ListaDeseos lista) {
        return ResponseEntity.ok(listaDeseosService.crearLista(lista));
    }

    @GetMapping
    public ResponseEntity<List<ListaDeseos>> obtenerTodas() {
        return ResponseEntity.ok(listaDeseosService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaDeseos> obtenerPorId(@PathVariable Long id) {
        return listaDeseosService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/agregar-inmueble")
    public ResponseEntity<ListaDeseos> agregarInmueble(@PathVariable Long id, @RequestBody Inmueble inmueble) {
        return ResponseEntity.ok(listaDeseosService.agregarInmueble(id, inmueble));
    }

    @PutMapping("/{id}/eliminar-inmueble")
    public ResponseEntity<ListaDeseos> eliminarInmueble(@PathVariable Long id, @RequestBody Inmueble inmueble) {
        return ResponseEntity.ok(listaDeseosService.eliminarInmueble(id, inmueble));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLista(@PathVariable Long id) {
        listaDeseosService.eliminarLista(id);
        return ResponseEntity.noContent().build();
    }
}
