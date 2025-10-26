package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private String metodo; // tarjeta, paypal, etc.

    @Column(nullable = false)
    private String estado; // PAGADO, REEMBOLSADO

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    public Pago() {}

    public Pago(Reserva reserva, double monto, String metodo, String estado, LocalDateTime fechaPago) {
        this.reserva = reserva;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
        this.fechaPago = fechaPago;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
}