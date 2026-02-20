package com.example.backend.service;

import java.util.List;
import java.util.Optional;

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
        return usuarioRepository.existsByEmail(data) || usuarioRepository.existsByUsername(data);
    }

    @Override
    public Optional<Usuario> findByEmailOrUsername(String identifier) {
        if (identifier.contains("@")) {
            return Optional.ofNullable(usuarioRepository.findByEmail(identifier));
        } else {
            return Optional.ofNullable(usuarioRepository.findByUsername(identifier));
        }
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario updateUsuario(Usuario newUsuario, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;

        usuario.setUsername(newUsuario.getUsername());
        usuario.setSurname(newUsuario.getSurname());
        usuario.setAddress(newUsuario.getAddress());
        usuario.setEmail(newUsuario.getEmail());
        usuario.setPassword(newUsuario.getPassword());
        if (newUsuario.getRol() != null) {
            usuario.setRol(newUsuario.getRol());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}