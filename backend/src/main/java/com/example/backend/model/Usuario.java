package com.example.backend.model;

import jakarta.persistence.*;
/*
NO funciona y falta cambiar el diseño para diferenciar entre nombre de ususario y de persona 
Seguramente problema de H2, deberé volver a crear la tabla de usuario*/
@Entity
@Table(name = "Usuario")
public class Usuario {

    public enum Rol {
        INQUILINO, PROPIETARIO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.INQUILINO;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "pais", column = @Column(name = "pais", nullable = false)),
        @AttributeOverride(name = "ciudad", column = @Column(name = "ciudad", nullable = false)),
        @AttributeOverride(name = "codigoPostal", column = @Column(name = "codigo_postal", nullable = false)),
        @AttributeOverride(name = "calle", column = @Column(name = "calle", nullable = false)),
        @AttributeOverride(name = "edificio", column = @Column(name = "edificio", nullable = false)),
        @AttributeOverride(name = "piso", column = @Column(name = "piso", nullable = true))
    })
    private Direccion address;

    public Usuario() {}

    public Usuario(String username, String name, String surname, String email, String password, Direccion address, Rol rol) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Direccion getAddress() { return address; }
    public void setAddress(Direccion address) { this.address = address; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}