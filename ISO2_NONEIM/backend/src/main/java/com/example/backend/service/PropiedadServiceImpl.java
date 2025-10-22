package com.example.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.model.Propiedad;
import com.example.backend.repository.PropiedadRepository;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public List<Propiedad> listarPropiedades() {
        return propiedadRepository.findAll();
    }

    @Override
    public Propiedad crearPropiedad(Propiedad propiedad) {
        return propiedadRepository.save(propiedad);
    }

    @Override
    public void eliminarPropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }
}
ç