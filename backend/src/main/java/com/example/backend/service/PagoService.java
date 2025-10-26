
package com.example.backend.service;

import com.example.backend.model.Pago;
import com.example.backend.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    // Crear un nuevo pago
    public Pago crearPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    // Obtener todos los pagos
    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    // Buscar pago por referencia
    public Optional<Pago> obtenerPorReferencia(Long referencia) {
        return pagoRepository.findById(referencia);
    }

    // Actualizar un pago existente
    public Pago actualizarPago(Long referencia, Pago nuevoPago) {
        return pagoRepository.findById(referencia)
                .map(pago -> {
                    pago.setMetodoPago(nuevoPago.getMetodoPago());
                    pago.setReserva(nuevoPago.getReserva());
                    return pagoRepository.save(pago);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    // Eliminar un pago
    public void eliminarPago(Long referencia) {
        pagoRepository.deleteById(referencia);
    }
}
