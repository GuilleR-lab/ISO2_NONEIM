package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    private final PasswordEncoder passwordEncoder;
    
    //Dependence inyect
    public UsuarioServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    //aqui que no hay logica de negocio, lo puedo obtener directamente desde el repository??
    @Override
    public List<Usuario> showAllUsuarios() {
        return usuarioRepository.findAll();
    }
    
    //esta funcion tiene sentido
    @Override
    public Usuario authenticate(String data, String password) {
        Usuario usuario = usuarioRepository.existsByEmail(data) || usuarioRepository.existsByUsername(data)
        .orElseThrow( () -> new BadCredentialsException("Credenciales invalidas"));

        
        if (!passwordEncoder.matches(password, usuario.getPassword())){
            throw new BadCredentialsException("Credenciales invalidas");
        }

        return usuario;
    }

    //TODO: Buena practica crear aqui funcion de registro????
    
    //esta funcion tiene sentido
    @Override
    public Usuario createUsuario(Usuario usuario) {
        //hashing password
        String hash = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(hash);

        return usuarioRepository.save(usuario);
    }

    //@Override
    //public boolean usuarioExists(String data) {
    //    return usuarioRepository.existsByEmail(data) || usuarioRepository.existsByUsername(data);
    //}

    //Revisar, creo que esta funcion no tiene mucho sentido.
    @Override
    public Optional<Usuario> findByEmailOrUsername(String identifier) {
        if (identifier.contains("@")) {
            return Optional.ofNullable(usuarioRepository.findByEmail(identifier));
        } else {
            return Optional.ofNullable(usuarioRepository.findByUsername(identifier));
        }
    }
    
    //aqui que no hay logica de negocio, lo puedo obtener directamente desde el repository??
    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    //esta funcion tiene sentido
    @Override
    public Usuario updateUsuario(Usuario newUsuario, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;

        usuario.setUsername(newUsuario.getUsername());
        usuario.setSurname(newUsuario.getSurname());
        usuario.setAddress(newUsuario.getAddress());
        usuario.setEmail(newUsuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(newUsuario.getPassword())); //hashing new password

        if (newUsuario.getRol() != null) {
            usuario.setRol(newUsuario.getRol());
        }

        return usuarioRepository.save(usuario);
    }
    
    //esta funcion tiene sentido
    @Override
    public void deleteUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
