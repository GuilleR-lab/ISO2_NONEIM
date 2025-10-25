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
    public boolean usuarioExists(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /*@Override
        public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario findByEmail(String email){
        return usuarioRepository.findByEmail(email);
    }*/
    //Update usuario
    @Override
    public Usuario updateUsuario(Long usuarioId, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;

        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setSurname(usuarioActualizado.getSurname());
        usuario.setAddress(usuarioActualizado.getAddress());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setPassword(usuarioActualizado.getPassword());

        return usuarioRepository.save(usuario);
    }



    @Override
    public void deleteUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
