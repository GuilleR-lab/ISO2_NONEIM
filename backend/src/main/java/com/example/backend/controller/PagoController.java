
package com.example.backend.controller;

import com.example.backend.model.Pago;
import com.example.backend.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Crear un nuevo pago
    @PostMapping
    public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.crearPago(pago));
    }

    // Obtener todos los pagos
    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    // Obtener pago por referencia
    @GetMapping("/{referencia}")
    public ResponseEntity<Pago> obtenerPorReferencia(@PathVariable Long referencia) {
        return pagoService.obtenerPorReferencia(referencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar pago
    @PutMapping("/{referencia}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long referencia, @RequestBody Pago pago) {
        try {
            return ResponseEntity.ok(pagoService.actualizarPago(referencia, pago));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar pago
    @DeleteMapping("/{referencia}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long referencia) {
        pagoService.eliminarPago(referencia);
        return ResponseEntity.noContent().build();
    }
}
