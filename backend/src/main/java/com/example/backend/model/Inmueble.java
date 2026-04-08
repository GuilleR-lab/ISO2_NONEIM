package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Inmueble {

    public enum Tipo {
        VIVIENDA_COMPLETA, HABITACION, APARTAMENTO, ESTUDIO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInmueble;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private double precioNoche;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo = Tipo.VIVIENDA_COMPLETA;

    @Column(length = 1000)
    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_id")
    private Usuario propietario;

    @ManyToOne
    @JoinColumn(name = "idPolitica")
    private PoliticaCancelacion politicaCancelacion;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("inmueble")
    private List<Disponibilidad> disponibilidades;


    @JsonIgnore
    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    public Inmueble() {}

    public Inmueble(String direccion, String ciudad, double precioNoche, Tipo tipo, String descripcion, Usuario propietario) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.precioNoche = precioNoche;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.propietario = propietario;
    }

    public Long getIdInmueble() { return idInmueble; }
    public void setIdInmueble(Long idInmueble) { this.idInmueble = idInmueble; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public double getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(double precioNoche) { this.precioNoche = precioNoche; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }
    public PoliticaCancelacion getPoliticaCancelacion() { return politicaCancelacion; }
    public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) { this.politicaCancelacion = politicaCancelacion; }
    public List<Disponibilidad> getDisponibilidades() { return disponibilidades; }
    public void setDisponibilidades(List<Disponibilidad> disponibilidades) { this.disponibilidades = disponibilidades; }
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}