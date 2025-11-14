package com.example.backend.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.backend.model.Usuario;

@Service
public class SessionService {

    private Map<String, Usuario> sesiones = new ConcurrentHashMap<>();

    public void storeSession(String token, Usuario usuario) {
        sesiones.put(token, usuario);
    }

    public Usuario getUsuarioFromToken(String token) {
        return sesiones.get(token);
    }

    public void deleteSession(String token) {
        sesiones.remove(token);
    }
}


