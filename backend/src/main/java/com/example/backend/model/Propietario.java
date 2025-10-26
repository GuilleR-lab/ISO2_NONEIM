package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Propietario")
public class Propietario extends Usuario {

    @Column(nullable = false)
    private String telefono;

    public Propietario() {}

    public Propietario(String nombre, String email, String contraseña, String telefono) {
        contraseña);
        this.telefono = telefono;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
