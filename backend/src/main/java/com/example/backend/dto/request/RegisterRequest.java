package com.example.backend.dto.request;

import com.example.backend.model.Direccion;
import com.example.backend.model.Usuario.Rol;

public class RegisterRequest {
    private String username;
    private String name;
    private String surname;
    private String email;
    private Direccion address;
    private String password;
    private Rol rol;

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
