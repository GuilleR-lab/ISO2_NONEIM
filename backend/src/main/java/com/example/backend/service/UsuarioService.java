package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import com.example.backend.model.Usuario;

public interface UsuarioService {
    Usuario authenticate(String data, String password);
    List<Usuario> showAllUsuarios();
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Usuario usuario, Long usuarioId);
    void deleteUsuario(Long usuarioId);
    //boolean usuarioExists(String emailOrUsername);
    Optional<Usuario> findByEmailOrUsername(String identifier);
    Optional<Usuario> findById(Long id);
}
