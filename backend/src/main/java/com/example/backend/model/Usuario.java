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

    private String nombre;
    private String apellidos;
    private String direccion;

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
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public String setApellidos(String apellidos){
        this.apellidos = apellidos
    }

    public String getApellidos(){
        return apellidos
    }

    public String setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getDireccion(){
        return direccion;
    }
}
