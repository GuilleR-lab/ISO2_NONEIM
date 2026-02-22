package com.example.backend.model;

import jakarta.persistence.*;

@Embeddable
public class Direccion{

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String edificio;

    private String piso;

    public Direccion(){}

    public Direccion(String pais, String ciudad, String codigoPostal, String calle, String edificio, String piso){
        this.pais = pais;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.edificio = edificio;
        this.piso = piso;
    }

    // Getters y Setters
    public String getPais(){
        return pais;
    }

    public String getCiudad(){
        return ciudad;
    }

    public String getCodigoPostal(){
        return codigoPostal;
    }

    public String getCalle(){
        return calle;
    }

    public String getEdificio(){
        return edificio;
    }

    public String getPiso(){
        return piso;
    }

    public void setPais(String pais){
        this.pais = pais;
    }

    public void setCiudad(String ciudad){
        this.ciudad = ciudad;
    }

    public void setCodigoPostal(String codigoPostal){
        this.codigoPostal = codigoPostal;
    }

    public void setCalle(String calle){
        this.calle = calle;
    }

    public void setEdificio(String edificio){
        this.edificio = edificio;
    }

    public void setPiso(String piso){
        this.piso = piso;
    }
}