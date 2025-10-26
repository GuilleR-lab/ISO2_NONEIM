
package com.example.backend.service;

import com.example.backend.model.Inmueble;
import com.example.backend.repository.InmuebleRepository;
import com.example.backend.service.InmuebleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InmuebleServiceImpl implements InmuebleService {

    private final InmuebleRepository inmuebleRepository;

    public InmuebleServiceImpl(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    @Override
    public Inmueble crearInmueble(Inmueble inmueble) {
        return inmuebleRepository.save(inmueble);
    }

    @Override
    public List<Inmueble> obtenerTodos() {
        return inmuebleRepository.findAll();
    }

    @Override
    public Optional<Inmueble> obtenerPorId(Long id) {
        return inmuebleRepository.findById(id);
    }

    @Override
    public Inmueble actualizarInmueble(Long id, Inmueble nuevoInmueble) {
        return inmuebleRepository.findById(id)
                .map(inmueble -> {
                    inmueble.setDireccion(nuevoInmueble.getDireccion());
                    inmueble.setPrecioNoche(nuevoInmueble.getPrecioNoche());
                    inmueble.setPropietario(nuevoInmueble.getPropietario());
                    return inmuebleRepository.save(inmueble);
                })
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));
    }

    @Override
    public void eliminarInmueble(Long id) {
        if (!inmuebleRepository.existsById(id)) {
            throw new RuntimeException("Inmueble no encontrado");
        }
        inmuebleRepository.deleteById(id);
    }
}

