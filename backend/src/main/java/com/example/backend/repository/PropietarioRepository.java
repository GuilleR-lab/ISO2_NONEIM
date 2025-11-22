package com.example.backend.repository;

import com.example.backend.model.Propietario;
import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    boolean existsByUsuario(Usuario usuario);
    Propietario findByUsuario(Usuario usuario);
    void deleteByUsuario(Usuario usuario);
    
}
