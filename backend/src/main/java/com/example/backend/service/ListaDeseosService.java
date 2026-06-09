package com.example.backend.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Inmueble;
import com.example.backend.model.ListaDeseos;
import com.example.backend.repository.InmuebleRepository;
import com.example.backend.repository.ListaDeseosRepository;
import com.example.backend.repository.UsuarioRepository;

@Service
@Transactional
public class ListaDeseosService {

    private final ListaDeseosRepository listaDeseosRepository;
    private final InmuebleRepository inmuebleRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaDeseosService(ListaDeseosRepository listaDeseosRepository, InmuebleRepository inmuebleRepository, UsuarioRepository usuarioRepository) {
        this.listaDeseosRepository = listaDeseosRepository;
        this.inmuebleRepository = inmuebleRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Crear lista
    public ListaDeseos crearLista(ListaDeseos lista) {
        if (lista == null || lista.getUsuario() == null || lista.getUsuario().getId() == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Long usuarioId = lista.getUsuario().getId();
        return obtenerPorUsuario(usuarioId);
    }

    public ListaDeseos obtenerPorUsuario(Long usuarioId) {
        List<ListaDeseos> listas = listaDeseosRepository.findAllByUsuarioId(usuarioId);

        if (listas.isEmpty()) {
            var usuarioGestionado = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            return listaDeseosRepository.saveAndFlush(new ListaDeseos(usuarioGestionado));
        }

        ListaDeseos listaPrincipal = listas.get(0);
        if (listas.size() > 1) {
            LinkedHashSet<Inmueble> inmueblesUnicos = new LinkedHashSet<>(listaPrincipal.getInmuebles());
            for (int i = 1; i < listas.size(); i++) {
                inmueblesUnicos.addAll(listas.get(i).getInmuebles());
            }
            listaPrincipal.setInmuebles(inmueblesUnicos);
            for (int i = 1; i < listas.size(); i++) {
                listaDeseosRepository.delete(listas.get(i));
            }
            listaPrincipal = listaDeseosRepository.saveAndFlush(listaPrincipal);
        }

        return listaPrincipal;
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
        if (inmueble == null || inmueble.getIdInmueble() == null) {
            throw new RuntimeException("Inmueble no encontrado");
        }

        Inmueble inmueblePersistido = inmuebleRepository.findById(inmueble.getIdInmueble())
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        boolean yaExiste = lista.getInmuebles().stream()
                .anyMatch(item -> item.getIdInmueble().equals(inmueblePersistido.getIdInmueble()));

        if (!yaExiste) {
            lista.getInmuebles().add(inmueblePersistido);
        }
        return listaDeseosRepository.saveAndFlush(lista);
    }

    // Eliminar inmueble de la lista
    public ListaDeseos eliminarInmueble(Long idLista, Inmueble inmueble) {
        ListaDeseos lista = listaDeseosRepository.findById(idLista)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
        if (inmueble == null || inmueble.getIdInmueble() == null) {
            throw new RuntimeException("Inmueble no encontrado");
        }

        Long inmuebleId = inmueble.getIdInmueble();
        lista.getInmuebles().removeIf(item -> item.getIdInmueble().equals(inmuebleId));
        return listaDeseosRepository.saveAndFlush(lista);
    }

    // Eliminar lista
    public void eliminarLista(Long idLista) {
        if (!listaDeseosRepository.existsById(idLista)) {
            throw new RuntimeException("No se puede eliminar: Lista no encontrada");
        }
        listaDeseosRepository.deleteById(idLista);
    }
}
