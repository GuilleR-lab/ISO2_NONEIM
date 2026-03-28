package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referencia;

    @Column(nullable = false)
    private String metodoPago; // TARJETA_CREDITO, TARJETA_DEBITO, PAYPAL

    @Column(nullable = false)
    private double importe;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "idReserva", nullable = false)
    private Reserva reserva;

    public Pago() {}

    public Pago(String metodoPago, double importe, Reserva reserva) {
        this.metodoPago = metodoPago;
        this.importe = importe;
        this.reserva = reserva;
    }

    public Long getReferencia() { return referencia; }
    public void setReferencia(Long referencia) { this.referencia = referencia; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
}