package com.example.backend.service;

import com.example.backend.model.Inmueble;
import com.example.backend.repository.InmuebleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                inmueble.setCiudad(nuevoInmueble.getCiudad());
                inmueble.setPrecioNoche(nuevoInmueble.getPrecioNoche());
                inmueble.setTipo(nuevoInmueble.getTipo());
                inmueble.setDescripcion(nuevoInmueble.getDescripcion());
                inmueble.setPropietario(nuevoInmueble.getPropietario());
                inmueble.setPoliticaCancelacion(nuevoInmueble.getPoliticaCancelacion());
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

    @Override
    public List<Inmueble> buscarConFiltros(String ciudad, Inmueble.Tipo tipo, boolean soloDirecta, LocalDate fechaInicio, LocalDate fechaFin) {
        return inmuebleRepository.buscarConFiltros(ciudad, tipo, soloDirecta, fechaInicio, fechaFin);
    }

    @Override
    public List<Inmueble> obtenerPorPropietario(Long propietarioId) {
        return inmuebleRepository.findByPropietarioId(propietarioId);
    }
}