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
        return (List<Usuario>) usuarioRepository.findAll(); 
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean usuarioExits(String email) {
        return usuarioRepository.findByEmail(email) != null;
    }

    //TODO: Update usuario
    @Override
    public Usuario updateUsuario(Usuario usuario, Long usuarioId) {
        return null;
    }

    @Override
    public void deleteUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
