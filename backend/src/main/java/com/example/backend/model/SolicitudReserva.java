
package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SolicitudReserva")
public class SolicitudReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;

    @Column(nullable = false)
    private LocalDate fechaSolicitud = LocalDate.now();

    @Column(nullable = false)
    private String estado; // PENDIENTE, ACEPTADA, RECHAZADA

    // Relación con Usuario (N:1)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación con Disponibilidad (N:1)
    @ManyToOne
    @JoinColumn(name = "idDisponibilidad", nullable = false)
    private Disponibilidad disponibilidad;

    // Relación opcional con Reserva (1:0..1)
    @OneToOne(mappedBy = "solicitudReserva", cascade = CascadeType.ALL)
    private Reserva reserva;

    public SolicitudReserva() {}

    public SolicitudReserva(String estado, Usuario usuario, Disponibilidad disponibilidad) {
        this.estado = estado;
        this.usuario = usuario;
        this.disponibilidad = disponibilidad;
    }

    // Getters y Setters
    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }

    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
}
