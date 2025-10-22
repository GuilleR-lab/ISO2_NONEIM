package com.example.backend.model;

import jakarta.persistence.*;

@Entity
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private double precioPorNoche;

    @Column(nullable = false)
    private boolean reservaInmediata;  // true = reserva directa, false = solicitud manual

    @Column(nullable = false)
    private String tipo; // Ej: "vivienda completa", "habitación privada", etc.

    @Column(nullable = false)
    private String politicaCancelacion; // Ej: flexible, moderada, estricta

    // Relación con el propietario (Usuario)
    @ManyToOne
    @JoinColumn(name = "propietario_id", nullable = false)
    private Usuario propietario;

    // --- Constructores ---
    public Propiedad() {}

    public Propiedad(String titulo, String descripcion, String direccion, String ciudad,
                     String pais, double precioPorNoche, boolean reservaInmediata,
                     String tipo, String politicaCancelacion, Usuario propietario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.precioPorNoche = precioPorNoche;
        this.reservaInmediata = reservaInmediata;
        this.tipo = tipo;
        this.politicaCancelacion = politicaCancelacion;
        this.propietario = propietario;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public double getPrecioPorNoche() { return precioPorNoche; }
    public void setPrecioPorNoche(double precioPorNoche) { this.precioPorNoche = precioPorNoche; }

    public boolean isReservaInmediata() { return reservaInmediata; }
    public void setReservaInmediata(boolean reservaInmediata) { this.reservaInmediata = reservaInmediata; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPoliticaCancelacion() { return politicaCancelacion; }
    public void setPoliticaCancelacion(String politicaCancelacion) { this.politicaCancelacion = politicaCancelacion; }

    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }
}