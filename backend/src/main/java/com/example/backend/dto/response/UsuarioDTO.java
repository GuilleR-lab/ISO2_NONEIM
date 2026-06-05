package com.example.backend.dto.response;

import com.example.backend.model.Usuario.Rol;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;  
    private Rol rol;

    //constructor
    public UsuarioDTO (Long id, String username, String email, Rol rol) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
    }
    
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
