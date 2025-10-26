package com.example.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PoliticaCancelacion")
public class PoliticaCancelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPolitica;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private double penalizacion; // porcentaje o monto

    // Relación con Reserva (1:N)
    @OneToMany(mappedBy = "politicaCancelacion", cascade = CascadeType.ALL)
    private Set<Reserva> reservas = new HashSet<>();

    public PoliticaCancelacion() {}

    public PoliticaCancelacion(String descripcion, double penalizacion) {
        this.descripcion = descripcion;
        this.penalizacion = penalizacion;
    }

    // Getters y Setters
    public Long getIdPolitica() { return idPolitica; }
    public void setIdPolitica(Long idPolitica) { this.idPolitica = idPolitica; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPenalizacion() { return penalizacion; }
    public void setPenalizacion(double penalizacion) { this.penalizacion = penalizacion; }

    public Set<Reserva> getReservas() { return reservas; }
    public void setReservas(Set<Reserva> reservas) { this.reservas = reservas; }
}
