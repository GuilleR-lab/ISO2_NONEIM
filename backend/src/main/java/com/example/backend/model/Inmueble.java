package com.example.backend.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInmueble;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private double precioNoche;

    // Relación con el propietario (hereda de Usuario)
    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    // Relación con Disponibilidad (1:N)
    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilidad> disponibilidades;

    // Relación con Reserva (1:N)
    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    public Inmueble() {}

    public Inmueble(String direccion, double precioNoche, Propietario propietario) {
        this.direccion = direccion;
        this.precioNoche = precioNoche;
        this.propietario = propietario;
    }

    // Getters y Setters
    public Long getIdInmueble() { return idInmueble; }
    public void setIdInmueble(Long idInmueble) { this.idInmueble = idInmueble; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public double getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(double precioNoche) { this.precioNoche = precioNoche; }

    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    public List<Disponibilidad> getDisponibilidades() { return disponibilidades; }
    public void setDisponibilidades(List<Disponibilidad> disponibilidades) { this.disponibilidades = disponibilidades; }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}
