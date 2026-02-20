package com.example.backend.model;

import jakarta.persistence.*;

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
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.INQUILINO;

    public Usuario() {}

    public Usuario(String username, String surname, String email, String password, String address, Rol rol) {
        this.username = username;
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
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}