package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre){
        return nombre;
    }

    public void getNombre(){
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos){
        return apellidos;
    }

    public void getApellidos(){
        this.apellidos = apellidos;
    }

    public void setDireccion(String direccion){
        return direccion;
    }

    public void getDireccion(){
        this.direccion = direccion;
    }
}
