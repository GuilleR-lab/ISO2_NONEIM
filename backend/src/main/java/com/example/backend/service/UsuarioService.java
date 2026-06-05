package com.example.backend.service;

//import java.util.List;
import java.util.Optional;

import com.example.backend.dto.response.UsuarioDTO;
import com.example.backend.model.Usuario;
import com.example.backend.dto.request.RegisterRequest;

public interface UsuarioService {
    UsuarioDTO authenticate(String data, String password);
    //List<UsuarioDTO> showAllUsuarios();
    Optional<Usuario> findById(Long id);
    void createUsuario(RegisterRequest usuario);
    void updateUsuario(Usuario usuario, Long usuarioId);
    void deleteUsuario(Long usuarioId);
}
