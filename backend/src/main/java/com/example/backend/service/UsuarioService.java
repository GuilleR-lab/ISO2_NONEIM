package com.example.backend.service;

import java.util.List;
import com.example.backend.model.Usuario;

public interface UsuarioService {
    //Usuario findById(Long id);
    Usuario findByEmail(String email);
    Usuario getUsuarioById(Long usuarioId);
    Usuario updateUsuario(Long usuarioId, Usuario usuarioActualizado);
    boolean usuarioExists(String email);
    Usuario createUsuario(Usuario usuario);
    List<Usuario> showAllUsuarios();
    void deleteUsuario(Long usuarioId);
}
