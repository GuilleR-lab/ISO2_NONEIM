package com.example.backend.model;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ListaDeseos")
public class ListaDeseos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLista;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "lista_inmuebles",
        joinColumns = @JoinColumn(name = "lista_id"),
        inverseJoinColumns = @JoinColumn(name = "inmueble_id")
    )
    private Set<Inmueble> inmuebles = new HashSet<>();

    public ListaDeseos() {}

    public ListaDeseos(Usuario usuario) {
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getIdLista() { return idLista; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Set<Inmueble> getInmuebles() { return inmuebles; }
    public void setInmuebles(Set<Inmueble> inmuebles) { this.inmuebles = inmuebles; }
}
