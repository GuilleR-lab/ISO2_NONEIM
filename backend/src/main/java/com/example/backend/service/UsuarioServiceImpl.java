package com.example.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> showAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean usuarioExists(String data) {
        return usuarioRepository.existsByEmail(data);
    }

    @Override
    public Usuario updateUsuario(Usuario newUsuario, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null) return null;

        usuario.setNombre(newUsuario.getNombre());
        usuario.setEmail(newUsuario.getEmail());
        usuario.setContraseña(newUsuario.getContraseña());

        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
