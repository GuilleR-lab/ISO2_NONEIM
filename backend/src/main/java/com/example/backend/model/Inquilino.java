package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Inquilino")
public class Inquilino extends Usuario {

    @Column(nullable = false)
    private String direccion;

    public Inquilino() {}

    public Inquilino(String nombre, String email, String contraseña, String direccion) {
        super(nombre, email, contraseña);
        this.direccion = direccion;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}