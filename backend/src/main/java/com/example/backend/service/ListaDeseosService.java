package com.example.backend.service;

import com.example.backend.model.ListaDeseos;
import com.example.backend.model.Inmueble;
import com.example.backend.repository.ListaDeseosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaDeseosService {

    private final ListaDeseosRepository listaDeseosRepository;

    public ListaDeseosService(ListaDeseosRepository listaDeseosRepository) {
        this.listaDeseosRepository = listaDeseosRepository;
    }

    // Crear lista
    public ListaDeseos crearLista(ListaDeseos lista) {
        return listaDeseosRepository.save(lista);
    }

    // Obtener todas las listas
    public List<ListaDeseos> obtenerTodas() {
        return listaDeseosRepository.findAll();
    }

    // Obtener lista por ID
    public Optional<ListaDeseos> obtenerPorId(Long idLista) {
        return listaDeseosRepository.findById(idLista);
    }

    // Agregar inmueble a la lista
    public ListaDeseos agregarInmueble(Long idLista, Inmueble inmueble) {
        ListaDeseos lista = listaDeseosRepository.findById(idLista)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
        lista.getInmuebles().add(inmueble);
        return listaDeseosRepository.save(lista);
    }

    // Eliminar inmueble de la lista
    public ListaDeseos eliminarInmueble(Long idLista, Inmueble inmueble) {
        ListaDeseos lista = listaDeseosRepository.findById(idLista)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
        lista.getInmuebles().remove(inmueble);
        return listaDeseosRepository.save(lista);
    }

    // Eliminar lista
    public void eliminarLista(Long idLista) {
        listaDeseosRepository.deleteById(idLista);
    }
}
