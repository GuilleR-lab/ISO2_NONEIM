package com.example.backend.service;

import com.example.backend.model.PoliticaCancelacion;
import com.example.backend.repository.PoliticaCancelacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoliticaCancelacionService {

    private final PoliticaCancelacionRepository politicaRepository;

    public PoliticaCancelacionService(PoliticaCancelacionRepository politicaRepository) {
        this.politicaRepository = politicaRepository;
    }

    public PoliticaCancelacion crearPolitica(PoliticaCancelacion politica) {
        return politicaRepository.save(politica);
    }

    public List<PoliticaCancelacion> obtenerTodas() {
        return politicaRepository.findAll();
    }

    public Optional<PoliticaCancelacion> obtenerPorId(Long id) {
        return politicaRepository.findById(id);
    }

    public PoliticaCancelacion actualizarPolitica(Long id, PoliticaCancelacion nuevaPolitica) {
        return politicaRepository.findById(id)
                .map(politica -> {
                    politica.setDescripcion(nuevaPolitica.getDescripcion());
                    politica.setPenalizacion(nuevaPolitica.getPenalizacion());
                    return politicaRepository.save(politica);
                })
                .orElseThrow(() -> new RuntimeException("Política no encontrada"));
    }

    public void eliminarPolitica(Long id) {
        politicaRepository.deleteById(id);
    }
}
