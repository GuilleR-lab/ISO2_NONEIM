package com.example.backend.service;

import java.util.List;
import com.example.backend.model.Usuario;

public interface UsuarioService {
    List<Usuario> showAllUsuarios();
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Usuario usuario, Long UsuarioId);
    void deleteUsuario(Long UsuarioId);
    boolean usuarioExits(String email);
}
