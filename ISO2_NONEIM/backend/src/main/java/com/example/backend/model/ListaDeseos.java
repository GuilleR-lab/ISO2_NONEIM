package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ListaDeseos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @Column(nullable = false)
    private LocalDate fechaAgregado = LocalDate.now();

    public ListaDeseos() {}

    public ListaDeseos(Usuario usuario, Propiedad propiedad) {
        this.usuario = usuario;
        this.propiedad = propiedad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
    public LocalDate getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDate fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}