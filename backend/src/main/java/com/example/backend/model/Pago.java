
package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referencia; // PK según el diagrama

    @Column(nullable = false)
    private String metodoPago; // tarjeta, paypal, etc.

    // Relación 1:1 con Reserva
    @OneToOne
    @JoinColumn(name = "idReserva", nullable = false)
    private Reserva reserva;

    public Pago() {}

    public Pago(String metodoPago, Reserva reserva) {
        this.metodoPago = metodoPago;
        this.reserva = reserva;
    }

    // Getters y Setters
    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
}
