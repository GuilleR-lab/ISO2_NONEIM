package com.example.backend.service;

//import java.util.List;
import java.util.Optional;

import com.example.backend.dto.response.UsuarioDTO;
import com.example.backend.model.Usuario;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.model.Direccion;

public interface UsuarioService {
    UsuarioDTO authenticate(String data, String password);
    //List<UsuarioDTO> showAllUsuarios();
    Optional<Usuario> findById(Long id); 
    void createUsuario(RegisterRequest usuario);
    UsuarioDTO updateUsuario(Usuario usuario, Long usuarioId);
    UsuarioDTO updatePassword(Long id, String passwordActual, String passwordNueva);
    UsuarioDTO updateDireccion(Long id, Direccion nuevaDireccion);
    void deleteUsuario(Long usuarioId);
}
