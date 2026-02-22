package com.example.backend.dto;

import com.example.backend.model.Direccion;

public class AuthDTO {

    public static class LoginRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String username;
        private String name;
        private String surname;
        private String email;
        private Direccion address;
        private String password;
        private String rol; // "INQUILINO" o "PROPIETARIO"

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
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }

    public static class LoginResponse {
        private String message;
        private Long id;
        private String username;
        private String email;
        private String rol;

        public LoginResponse(String message, Long id, String username, String email, String rol) {
            this.message = message;
            this.id = id;
            this.username = username;
            this.email = email;
            this.rol = rol;
        }

        public String getMessage() { return message; }
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRol() { return rol; }
    }
}