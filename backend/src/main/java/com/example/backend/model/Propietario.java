package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Propietario")
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    public Propietario() {}

    public Propietario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId(){
        return id;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

}    
