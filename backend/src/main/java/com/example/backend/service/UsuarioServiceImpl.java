package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.dto.response.UsuarioDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.model.Usuario.Rol;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    
    //Dependence inyect
    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }
    
    //TODO: Changue to return UsuarioDTO and Changue controllerInmueble
    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    //@Override
    //public List<UsuarioDTO> showAllUsuarios() {
    //    return usuarioRepository.findAll();
    //}
    
    @Override
    public UsuarioDTO authenticate(String data, String password) {
        
        Usuario usuario;
        
        //verify usuario exists
        if(usuarioRepository.findByUsername(data) == null && usuarioRepository.findByEmail(data) == null) {
            throw new BadCredentialsException("Credenciales invalidas");
        }
        
        //found usuario
        usuario = usuarioRepository.findByEmail(data); 
        if (usuario == null) usuario = usuarioRepository.findByUsername(data);

        //verify hashing password 
        if (!passwordEncoder.matches(password, usuario.getPassword())){
            throw new BadCredentialsException("Credenciales invalidas");
        }

        return new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getRol());
    }

    @Override
    public void createUsuario(RegisterRequest dto) {
        //usuario already exists 
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Este usuario ya existe");
        }

        //hashing password
        String hash = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hash);
        
        //dto.setRol(Rol.INQUILINO);
        
        //create Usuario object
        Usuario nuevo = new Usuario(
            dto.getUsername(),
            dto.getName(),
            dto.getSurname(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getAddress(), 
            dto.getRol()
        );

        usuarioRepository.save(nuevo);
    }
    
    @Override
    public void updateUsuario(Usuario newUsuario, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return;

        //Username, Email and Rol are always in DTO.
        if (newUsuario.getUsername() != usuario.getUsername()) usuario.setUsername(newUsuario.getUsername());
        if (newUsuario.getEmail() != usuario.getEmail()) usuario.setEmail(newUsuario.getEmail());
        if (newUsuario.getSurname() != null) usuario.setSurname(newUsuario.getSurname());
        if (newUsuario.getAddress() != null) usuario.setAddress(newUsuario.getAddress());
        if (newUsuario.getPassword() != null) usuario.setPassword(passwordEncoder.encode(newUsuario.getPassword())); //hashing new password 
        if (newUsuario.getRol() != usuario.getRol()) usuario.setRol(newUsuario.getRol());

        usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
