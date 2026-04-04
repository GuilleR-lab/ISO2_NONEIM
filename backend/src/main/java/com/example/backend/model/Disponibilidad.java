
package com.example.backend.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDisponibilidad;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private boolean directa; // true = reserva inmediata

    // Relación con Inmueble
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inmueble_id")
    @JsonIgnoreProperties({"disponibilidades", "propietario", "hibernateLazyInitializer", "handler"}) // Ignora las listas hijas para no entrar en bucle
    private Inmueble inmueble;

    public Disponibilidad() {}

    public Disponibilidad(LocalDate fechaInicio, LocalDate fechaFin, double precio, boolean directa, Inmueble inmueble) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precio = precio;
        this.directa = directa;
        this.inmueble = inmueble;
    }

    // Getters y Setters
    public Long getIdDisponibilidad() { return idDisponibilidad; }
    public void setIdDisponibilidad(Long idDisponibilidad) { this.idDisponibilidad = idDisponibilidad; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isDirecta() { return directa; }
    public void setDirecta(boolean directa) { this.directa = directa; }

    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }
}

